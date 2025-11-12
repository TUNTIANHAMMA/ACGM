package com.acgm.acgmmediatracker.dto.media;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.List;
import java.util.Objects;

/**
 * Service 层返回给上层的媒体详情，包含标签 ID 集合，用于后续 DTO 映射。
 */
public record MediaItemDetail(MediaItem mediaItem, List<Long> tagIds) {

    public MediaItemDetail {
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");
        tagIds = tagIds == null ? List.of() : List.copyOf(tagIds);
    }
}
