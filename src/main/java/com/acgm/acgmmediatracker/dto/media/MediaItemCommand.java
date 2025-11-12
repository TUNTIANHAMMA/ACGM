package com.acgm.acgmmediatracker.dto.media;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 媒体条目在 Service 层的输入载体，允许同时携带标签 ID 列表。
 */
public record MediaItemCommand(MediaItem mediaItem, List<Long> tagIds) {

    public MediaItemCommand {
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");
    }

    public boolean hasTagInstruction() {
        return tagIds != null;
    }

    public List<Long> tagIdsOrEmpty() {
        return tagIds == null ? Collections.emptyList() : tagIds;
    }
}
