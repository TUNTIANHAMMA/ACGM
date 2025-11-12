package com.acgm.acgmmediatracker.dto.media;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service 层接收媒体写入指令的 record
 * :
 * - {@link #mediaItem()}：必填的媒体实体，承载所有字段，供 insert/update 使用。
 * - {@link #tagIds()}：可选的标签 ID 列表，Controller 传入后 Service 可一次性同步标签。
 */
public record MediaItemCommand(MediaItem mediaItem, List<Long> tagIds) {

    /**
     * Record 紧凑构造器，保证 mediaItem 永远不为 null，避免 Service 层反复手工判空。
     */
    public MediaItemCommand {
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");
    }

    /**
     * 是否显式携带标签同步指令。未携带时 Service 将跳过标签操作，保持原有关系。
     */
    public boolean hasTagInstruction() {
        return tagIds != null;
    }

    /**
     * 返回标签 ID 列表，如果未传递则以空列表代替，方便下游调用 replaceTags 等逻辑。
     */
    public List<Long> tagIdsOrEmpty() {
        return tagIds == null ? Collections.emptyList() : tagIds;
    }
}
