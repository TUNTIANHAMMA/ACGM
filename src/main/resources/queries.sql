-- 暂不实现的数据库功能
-- 这部分是以后期望实现的功能，目前暂不作为项目开发文件之一







-- =========================================
-- 9) Query Templaste (COMMENTED OUT FOR DIRECT EXECUTION)
-- These are examples. They include placeholders and should NOT be run as-is.
-- Use the stored procedures in section 10 instead.
--
/*
-- 9.1 List media with common filters and pagination (TEMPLATE)
-- :u=user_id, :t=type?, :s=status?, :rmin=rating_min?, :kw=keyword?, :sort=finish_date/created_at, :dir=ASC/DESC
SELECT m.*
FROM media_items m
WHERE m.user_id = :u
AND (:t IS NULL OR m.type = :t)
AND (:s IS NULL OR m.status = :s)
AND (:rmin IS NULL OR (m.rating IS NOT NULL AND m.rating >= :rmin))
AND m.deleted_at IS NULL
AND (:kw IS NULL OR MATCH(m.title, m.notes) AGAINST (:kw IN BOOLEAN MODE))
ORDER BY m.finish_date DESC, m.id DESC
LIMIT :limit OFFSET :offset;


-- 9.2 Multi-tag AND filter (TEMPLATE)
WITH t AS (
SELECT id FROM tags WHERE user_id = :u AND name IN (:t1,:t2,:t3)
)
SELECT m.*
FROM media_items m
JOIN media_tag_rel r ON r.media_id = m.id
JOIN t ON t.id = r.tag_id
WHERE m.user_id = :u AND m.deleted_at IS NULL
GROUP BY m.id
HAVING COUNT(DISTINCT r.tag_id) = (SELECT COUNT(*) FROM t)
ORDER BY m.finish_date DESC, m.id DESC
LIMIT :limit OFFSET :offset;
*/

-- =========================================
-- 10) Stored Procedures (SAFE TO RUN)
-- =========================================
DELIMITER $$
DROP PROCEDURE IF EXISTS sp_media_list_basic $$
CREATE PROCEDURE sp_media_list_basic(
    IN p_user_id BIGINT UNSIGNED,
    IN p_type ENUM('anime','manga','game','music','movie','tv'),
    IN p_status ENUM('planned','in_progress','completed','dropped','on_hold'),
    IN p_rating_min DECIMAL(3,1),
    IN p_keyword VARCHAR(255),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    SELECT m.*
    FROM media_items m
    WHERE m.user_id = p_user_id
      AND (p_type IS NULL OR m.type = p_type)
      AND (p_status IS NULL OR m.status = p_status)
      AND (p_rating_min IS NULL OR (m.rating IS NOT NULL AND m.rating >= p_rating_min))
      AND m.deleted_at IS NULL
      AND (p_keyword IS NULL OR MATCH(m.title, m.notes) AGAINST (p_keyword IN BOOLEAN MODE))
    ORDER BY m.finish_date DESC, m.id DESC
    LIMIT p_limit OFFSET p_offset;
END $$


DROP PROCEDURE IF EXISTS sp_media_by_tags_and $$
CREATE PROCEDURE sp_media_by_tags_and(
    IN p_user_id BIGINT UNSIGNED,
    IN p_tags_csv TEXT, -- comma separated tag names: '冒险,热血'
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    WITH t AS (
        SELECT id
        FROM tags
        WHERE user_id = p_user_id
          AND (p_tags_csv IS NULL OR FIND_IN_SET(name, p_tags_csv) > 0)
    )
    SELECT m.*
    FROM media_items m
             JOIN media_tag_rel r ON r.media_id = m.id
             JOIN t ON t.id = r.tag_id
    WHERE m.user_id = p_user_id AND m.deleted_at IS NULL
    GROUP BY m.id
    HAVING COUNT(DISTINCT r.tag_id) = (SELECT COUNT(*) FROM t)
    ORDER BY m.finish_date DESC, m.id DESC
    LIMIT p_limit OFFSET p_offset;
END $$
DELIMITER ;


-- Quick usage examples:
-- CALL sp_media_list_basic(123, 'anime', NULL, NULL, '+EVA', 20, 0);
-- CALL sp_media_by_tags_and(123, '冒险,热血', 20, 0);


-- =========================================
-- End of file