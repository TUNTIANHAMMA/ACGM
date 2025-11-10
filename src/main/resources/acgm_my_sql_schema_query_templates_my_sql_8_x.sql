-- ACGM Media Tracker - MySQL 8.x schema & query templates
-- Charset/Collation and safe modes
SET NAMES utf8mb4;
SET SQL_SAFE_UPDATES = 0;

SET FOREIGN_KEY_CHECKS = 0;

-- Create database
CREATE DATABASE IF NOT EXISTS `acgm_tracker`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `acgm_tracker`;

-- =========================================
-- 1) Users
-- =========================================
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username      VARCHAR(32)     NOT NULL,
  email         VARCHAR(64)     NOT NULL,
  -- normalize for case/space-insensitive uniqueness
  email_norm    VARCHAR(64) GENERATED ALWAYS AS (LOWER(TRIM(email))) STORED,
  password      VARCHAR(255)    NOT NULL,
  role          ENUM('user','admin') NOT NULL DEFAULT 'user',
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_username (username),
  UNIQUE KEY uq_users_email (email),
  UNIQUE KEY uq_users_email_norm (email_norm)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 2) Media Items (core table)
-- =========================================
DROP TABLE IF EXISTS media_items;
CREATE TABLE media_items (
  id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id         BIGINT UNSIGNED NOT NULL,
  type            ENUM('anime','manga','game','music','movie','tv') NOT NULL,
  status          ENUM('planned','in_progress','completed','dropped','on_hold') NOT NULL DEFAULT 'planned',
  title           VARCHAR(255) NOT NULL,
  notes           TEXT NULL,
  rating          DECIMAL(3,1) NULL,
  start_date      DATE NULL,
  finish_date     DATE NULL,
  cover_url       VARCHAR(512) NULL,
  source          VARCHAR(32) NULL,
  created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at      DATETIME NULL,
  row_version     BIGINT UNSIGNED NOT NULL DEFAULT 0,
  -- helper generated column for grouping by month
  finish_month    INT GENERATED ALWAYS AS (CASE WHEN finish_date IS NULL THEN NULL ELSE (YEAR(finish_date)*100 + MONTH(finish_date)) END) STORED,
  PRIMARY KEY (id),
  KEY idx_media_u_t_s_fd (user_id, type, status, finish_date),
  KEY idx_media_u_t_rating (user_id, type, rating),
  KEY idx_media_u_s_fd (user_id, status, finish_date),
  KEY idx_media_not_deleted (deleted_at),
  KEY idx_media_finish_month (user_id, finish_month),
  FULLTEXT KEY ft_title_notes (title, notes) WITH PARSER ngram,
  CONSTRAINT fk_media_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT chk_media_rating_range CHECK (rating IS NULL OR (rating >= 0.0 AND rating <= 10.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 3) Tags and relation (many-to-many)
-- =========================================
DROP TABLE IF EXISTS media_tag_rel;
DROP TABLE IF EXISTS tags;

CREATE TABLE tags (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id    BIGINT UNSIGNED NOT NULL,
  name       VARCHAR(64) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_tags_user_name (user_id, name),
  CONSTRAINT fk_tags_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE media_tag_rel (
  media_id BIGINT UNSIGNED NOT NULL,
  tag_id   BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (media_id, tag_id),
  KEY idx_tag_media (tag_id, media_id),
  CONSTRAINT fk_mtr_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE,
  CONSTRAINT fk_mtr_tag   FOREIGN KEY (tag_id)   REFERENCES tags(id)        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 4) Favorites and Reviews
-- =========================================
DROP TABLE IF EXISTS favorites;
CREATE TABLE favorites (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id    BIGINT UNSIGNED NOT NULL,
  media_id   BIGINT UNSIGNED NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_fav_user_media (user_id, media_id),
  CONSTRAINT favorites_user_fk FOREIGN KEY (user_id)  REFERENCES users(id)        ON DELETE CASCADE,
  CONSTRAINT favorites_media_fk FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id    BIGINT UNSIGNED NOT NULL,
  media_id   BIGINT UNSIGNED NOT NULL,
  rating     DECIMAL(3,1) NULL,
  content    TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_reviews_media (media_id, created_at),
  CONSTRAINT reviews_user_fk FOREIGN KEY (user_id)  REFERENCES users(id)        ON DELETE CASCADE,
  CONSTRAINT reviews_media_fk FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE,
  CONSTRAINT chk_review_rating_range CHECK (rating IS NULL OR (rating >= 0.0 AND rating <= 10.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 5) External API cache
-- =========================================
DROP TABLE IF EXISTS media_api_info;
CREATE TABLE media_api_info (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  media_id   BIGINT UNSIGNED NOT NULL,
  api_source VARCHAR(32) NOT NULL,
  api_id     VARCHAR(128) NOT NULL,
  payload    JSON NULL,
  last_sync  DATETIME NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_api_source_id (api_source, api_id),
  UNIQUE KEY uq_media_source  (media_id, api_source),
  KEY idx_api_last_sync (last_sync),
  CONSTRAINT fk_api_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 6) Progress tables (1:1 with media_items by type)
-- =========================================
-- Anime progress
DROP TABLE IF EXISTS progress_anime;
CREATE TABLE progress_anime (
  media_id        BIGINT UNSIGNED NOT NULL,
  current_episode INT UNSIGNED NOT NULL DEFAULT 0,
  total_episodes  INT UNSIGNED NULL,
  updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (media_id),
  CONSTRAINT fk_pa_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Game progress
DROP TABLE IF EXISTS progress_game;
CREATE TABLE progress_game (
  media_id           BIGINT UNSIGNED NOT NULL,
  platform           VARCHAR(64) NULL,
  completion_percent TINYINT UNSIGNED NOT NULL DEFAULT 0,
  play_time_hours    INT UNSIGNED NOT NULL DEFAULT 0,
  updated_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (media_id),
  CONSTRAINT fk_pg_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE,
  CONSTRAINT chk_completion_percent CHECK (completion_percent BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Music progress
DROP TABLE IF EXISTS progress_music;
CREATE TABLE progress_music (
  media_id      BIGINT UNSIGNED NOT NULL,
  play_count    INT UNSIGNED NOT NULL DEFAULT 0,
  listen_status ENUM('listened','unheard','partial') NOT NULL DEFAULT 'unheard',
  updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (media_id),
  CONSTRAINT fk_pm_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 7) Triggers to enforce media.type consistency for progress tables
-- (Prevents inserting a progress row that doesn't match media_items.type)
-- =========================================
DELIMITER $$
DROP TRIGGER IF EXISTS trg_progress_anime_type_guard $$
CREATE TRIGGER trg_progress_anime_type_guard
BEFORE INSERT ON progress_anime
FOR EACH ROW
BEGIN
  IF (SELECT type FROM media_items WHERE id = NEW.media_id) <> 'anime' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be anime for progress_anime';
  END IF;
END $$

DROP TRIGGER IF EXISTS trg_progress_game_type_guard $$
CREATE TRIGGER trg_progress_game_type_guard
BEFORE INSERT ON progress_game
FOR EACH ROW
BEGIN
  IF (SELECT type FROM media_items WHERE id = NEW.media_id) <> 'game' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be game for progress_game';
  END IF;
END $$

DROP TRIGGER IF EXISTS trg_progress_music_type_guard $$
CREATE TRIGGER trg_progress_music_type_guard
BEFORE INSERT ON progress_music
FOR EACH ROW
BEGIN
  IF (SELECT type FROM media_items WHERE id = NEW.media_id) <> 'music' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be music for progress_music';
  END IF;
END $$
DELIMITER ;

-- =========================================
-- 8) Views (optional helpers)
-- =========================================
DROP VIEW IF EXISTS v_media_public;
CREATE VIEW v_media_public AS
SELECT id, user_id, type, status, title, rating, start_date, finish_date, cover_url
FROM media_items
WHERE deleted_at IS NULL;


SET FOREIGN_KEY_CHECKS = 1;


-- =========================================
-- 9) Query Templates
-- =========================================

-- 9.1 List media with common filters and pagination
-- :u=user_id, :t=type?, :s=status?, :rmin=rating_min?, :kw=keyword?, :sort=finish_date/created_at, :dir=ASC/DESC
-- NOTE: Use dynamic SQL in app; templates show patterns.

-- Keyword Boolean mode (MySQL 8 ngram fulltext) + filters + sort
-- If :kw is provided, use MATCH AGAINST; else omit the predicate.
/* Example (finish date desc): */
-- SELECT m.*
-- FROM media_items m
-- WHERE m.user_id = :u
--   AND (:t IS NULL OR m.type = :t)
--   AND (:s IS NULL OR m.status = :s)
--   AND (:rmin IS NULL OR (m.rating IS NOT NULL AND m.rating >= :rmin))
--   AND m.deleted_at IS NULL
--   AND (:kw IS NULL OR MATCH(m.title, m.notes) AGAINST (:kw IN BOOLEAN MODE))
-- ORDER BY m.finish_date DESC, m.id DESC
-- LIMIT :limit OFFSET :offset;

-- -- 9.2 Multi-tag AND filter
-- -- Given tag names [:t1,:t2,...], return media that contain ALL tags
-- WITH t AS (
--   SELECT id FROM tags WHERE user_id = :u AND name IN (:t1,:t2,:t3)
-- )
-- SELECT m.*
-- FROM media_items m
-- JOIN media_tag_rel r ON r.media_id = m.id
-- JOIN t ON t.id = r.tag_id
-- WHERE m.user_id = :u AND m.deleted_at IS NULL
-- GROUP BY m.id
-- HAVING COUNT(DISTINCT r.tag_id) = (SELECT COUNT(*) FROM t)
-- ORDER BY m.finish_date DESC, m.id DESC
-- LIMIT :limit OFFSET :offset;

-- -- 9.3 Insert + optimistic concurrency update examples
-- -- Insert a media item
-- INSERT INTO media_items (user_id, type, status, title, start_date)
-- VALUES (:u, 'anime', 'planned', :title, :start_date);

-- -- Update rating with optimistic locking
-- UPDATE media_items
-- SET rating = :new_rating,
--     row_version = row_version + 1
-- WHERE id = :media_id AND row_version = :current_row_version;
-- -- Check affected rows == 1; otherwise, concurrency conflict.

-- -- 9.4 Tag attach/detach
-- -- Attach (idempotent)
-- INSERT IGNORE INTO media_tag_rel (media_id, tag_id) VALUES (:mid, :tid);
-- -- Detach
-- DELETE FROM media_tag_rel WHERE media_id = :mid AND tag_id = :tid;

-- -- 9.5 Full-text search only (for debug/testing)
-- SELECT id, title, MATCH(title, notes) AGAINST (:kw IN BOOLEAN MODE) AS relevance
-- FROM media_items
-- WHERE MATCH(title, notes) AGAINST (:kw IN BOOLEAN MODE)
--   AND user_id = :u AND deleted_at IS NULL
-- ORDER BY relevance DESC
-- LIMIT 50;

-- -- 9.6 Monthly completion stats (per user)
-- SELECT finish_month, COUNT(*) AS cnt, AVG(rating) AS avg_rating
-- FROM media_items
-- WHERE user_id = :u AND deleted_at IS NULL AND finish_month IS NOT NULL
-- GROUP BY finish_month
-- ORDER BY finish_month DESC;

-- -- =========================================
-- -- End of file
