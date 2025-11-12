package com.acgm.acgmmediatracker.dto.media;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.List;
import java.util.Objects;

/**
 * Service 层往上游返回的媒体详情 record
 * ：
 * - {@link #mediaItem()}：数据库中的完整媒体实体，包含基础字段/进度入口等信息。
 * - {@link #tagIds()}：与该媒体关联的标签 ID 列表，供 Controller 填充响应 DTO。
 */
public record MediaItemDetail(MediaItem mediaItem, List<Long> tagIds) {

    /**
     * 构造时确保 mediaItem 非空，同时将标签列表规整为不可变集合，避免调用方意外修改。
     */
    public MediaItemDetail {
        // 紧凑构造器，略形参列表，直接操作定义时的组件
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");

        tagIds = (tagIds == null)
                ? List.of()
                : List.copyOf(tagIds);
    }
}
