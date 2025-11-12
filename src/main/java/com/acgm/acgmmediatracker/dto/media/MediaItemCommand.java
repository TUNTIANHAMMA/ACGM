package com.acgm.acgmmediatracker.dto.media;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * **媒体条目** 及其 **相关联标签** 在 Service 层的输入载体 Controller → Service
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
