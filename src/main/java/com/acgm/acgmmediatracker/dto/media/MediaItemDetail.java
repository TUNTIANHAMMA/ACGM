package com.acgm.acgmmediatracker.dto.media;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.List;
import java.util.Objects;

/**
 * 封装媒体条目及其关联的标签 ID 集合。Service 层的输出载体 Service → Controller
 */
public record MediaItemDetail(MediaItem mediaItem, List<Long> tagIds) {

    public MediaItemDetail {
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");
        tagIds = tagIds == null ? List.of() : List.copyOf(tagIds);
    }
}
