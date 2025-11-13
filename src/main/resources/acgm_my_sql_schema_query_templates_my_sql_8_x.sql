-- ACGM Media Tracker - MySQL 8.x schema & query templates
-- Charset/Collation and safe modes
SET NAMES utf8mb4;
SET SQL_SAFE_UPDATES = 0;

SET FOREIGN_KEY_CHECKS = 0;

-- Create database
CREATE DATABASE IF NOT EXISTS `acgm`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `acgm`;

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
  preference    JSON NULL,
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
-- 2) Media Library (global catalog)
-- =========================================
DROP TABLE IF EXISTS media_library;
CREATE TABLE media_library (
  id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  type           ENUM('anime','comic','game','music','movie','tv') NOT NULL,
  title          VARCHAR(255) NOT NULL,
  original_title VARCHAR(255) NULL,
  year           SMALLINT UNSIGNED NULL,
  cover_url      VARCHAR(512) NULL,
  source         VARCHAR(32) NULL,
  meta           JSON NULL,
  created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_ml_type_title (type, title),
  FULLTEXT KEY ft_ml_title (title, original_title) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 3) Media Items (user media list)
-- =========================================
DROP TABLE IF EXISTS media_items;
CREATE TABLE media_items (
  id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id         BIGINT UNSIGNED NOT NULL,
  library_id      BIGINT UNSIGNED NOT NULL,
  status          ENUM('planned','in_progress','completed','dropped','on_hold') NOT NULL DEFAULT 'planned',
  rating          DECIMAL(3,1) NULL,
  notes           TEXT NULL,
  start_date      DATE NULL,
  finish_date     DATE NULL,
  custom_title    VARCHAR(255) NULL,
  custom_cover_url VARCHAR(512) NULL,
  custom_source   VARCHAR(32) NULL,
  created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at      DATETIME NULL,
  row_version     BIGINT UNSIGNED NOT NULL DEFAULT 0,
  -- helper generated column for grouping by month
  finish_month    INT GENERATED ALWAYS AS (CASE WHEN finish_date IS NULL THEN NULL ELSE (YEAR(finish_date)*100 + MONTH(finish_date)) END) STORED,
  PRIMARY KEY (id),
  KEY idx_media_user_status_finish (user_id, status, finish_date),
  KEY idx_media_user_rating (user_id, rating),
  KEY idx_media_not_deleted (deleted_at),
  KEY idx_media_finish_month (user_id, finish_month),
  KEY idx_media_library (library_id),
  FULLTEXT KEY ft_media_custom_notes (custom_title, notes) WITH PARSER ngram,
  CONSTRAINT fk_media_library FOREIGN KEY (library_id)
    REFERENCES media_library(id) ON DELETE RESTRICT,
  CONSTRAINT fk_media_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT chk_media_rating_range CHECK (rating IS NULL OR (rating >= 0.0 AND rating <= 10.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 4) Tags and relation (many-to-many)
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
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (media_id, tag_id),
  KEY idx_tag_media (tag_id, media_id),
  CONSTRAINT fk_mtr_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE,
  CONSTRAINT fk_mtr_tag   FOREIGN KEY (tag_id)   REFERENCES tags(id)        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =========================================
-- 5) Favorites and Reviews
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
-- 6) External API cache
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
-- 7) Progress tables (1:1 with media_items by type)
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

-- Comic/Manga progress
DROP TABLE IF EXISTS progress_comic;
CREATE TABLE progress_comic (
  media_id         BIGINT UNSIGNED NOT NULL,
  current_chapter  INT UNSIGNED NOT NULL DEFAULT 0,
  total_chapters   INT UNSIGNED NULL,
  current_volume   INT UNSIGNED NULL,
  updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (media_id),
  CONSTRAINT fk_pc_media FOREIGN KEY (media_id) REFERENCES media_items(id) ON DELETE CASCADE
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
-- 8) Triggers to enforce media.type consistency for progress tables
-- (Prevents inserting a progress row that doesn't match media_library.type)
-- =========================================
DELIMITER $$
DROP TRIGGER IF EXISTS trg_progress_anime_type_guard $$
CREATE TRIGGER trg_progress_anime_type_guard
BEFORE INSERT ON progress_anime
FOR EACH ROW
BEGIN
  IF (SELECT l.type
      FROM media_items m
      JOIN media_library l ON l.id = m.library_id
      WHERE m.id = NEW.media_id) <> 'anime' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be anime for progress_anime';
  END IF;
END $$

DROP TRIGGER IF EXISTS trg_progress_comic_type_guard $$
CREATE TRIGGER trg_progress_comic_type_guard
BEFORE INSERT ON progress_comic
FOR EACH ROW
BEGIN
  IF (SELECT l.type
      FROM media_items m
      JOIN media_library l ON l.id = m.library_id
      WHERE m.id = NEW.media_id) <> 'comic' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be comic for progress_comic';
  END IF;
END $$

DROP TRIGGER IF EXISTS trg_progress_game_type_guard $$
CREATE TRIGGER trg_progress_game_type_guard
BEFORE INSERT ON progress_game
FOR EACH ROW
BEGIN
  IF (SELECT l.type
      FROM media_items m
      JOIN media_library l ON l.id = m.library_id
      WHERE m.id = NEW.media_id) <> 'game' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be game for progress_game';
  END IF;
END $$

DROP TRIGGER IF EXISTS trg_progress_music_type_guard $$
CREATE TRIGGER trg_progress_music_type_guard
BEFORE INSERT ON progress_music
FOR EACH ROW
BEGIN
  IF (SELECT l.type
      FROM media_items m
      JOIN media_library l ON l.id = m.library_id
      WHERE m.id = NEW.media_id) <> 'music' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'media.type must be music for progress_music';
  END IF;
END $$
DELIMITER ;

-- =========================================
-- 9) Views (optional helpers)
-- =========================================
DROP VIEW IF EXISTS v_media_public;
CREATE VIEW v_media_public AS
SELECT
  m.id,
  m.user_id,
  l.type,
  m.status,
  COALESCE(m.custom_title, l.title) AS title,
  m.rating,
  m.start_date,
  m.finish_date,
  COALESCE(m.custom_cover_url, l.cover_url) AS cover_url
FROM media_items m
JOIN media_library l ON l.id = m.library_id
WHERE m.deleted_at IS NULL;


SET FOREIGN_KEY_CHECKS = 1;



-- -- End of file
