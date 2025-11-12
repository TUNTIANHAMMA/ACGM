package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.dto.media.MediaItemCommand;
import com.acgm.acgmmediatracker.dto.media.MediaItemDetail;
import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.mapper.MediaItemMapper;
import com.acgm.acgmmediatracker.service.MediaTagRelationService;
import com.acgm.acgmmediatracker.service.MediaItemService;
import com.acgm.acgmmediatracker.service.exception.ResourceNotFoundException;
import com.acgm.acgmmediatracker.service.util.ServiceBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaItemServiceImpl implements MediaItemService {

    private final MediaItemMapper mediaItemMapper;
    private final MediaTagRelationService mediaTagRelationService;

    @Override
    public MediaItemDetail getDetail(long id) {
        return toDetail(loadMedia(id));
    }

    @Override
    public List<MediaItemDetail> listAll() {
        List<MediaItem> items = mediaItemMapper.selectAll();
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toDetail)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MediaItemDetail create(MediaItemCommand command) {
        Objects.requireNonNull(command, "媒体条目指令不能为空");
        MediaItem mediaItem = command.mediaItem();
        mediaItemMapper.insert(mediaItem);
        syncTags(mediaItem.getId(), command);
        return getDetail(mediaItem.getId());
    }

    @Override
    @Transactional
    public MediaItemDetail update(long id, MediaItemCommand command) {
        Objects.requireNonNull(command, "媒体条目指令不能为空");
        MediaItem existing = loadMedia(id);
        ServiceBeanUtils.copyNonNullProperties(command.mediaItem(), existing);
        existing.setId(id);
        mediaItemMapper.update(existing);
        syncTags(id, command);
        return getDetail(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        loadMedia(id);
        mediaTagRelationService.replaceTags(id, Collections.emptyList());
        mediaItemMapper.delete(id);
    }

    private MediaItem loadMedia(long id) {
        MediaItem mediaItem = mediaItemMapper.selectById(id);
        if (mediaItem == null) {
            throw new ResourceNotFoundException("未找到媒体条目，ID=" + id);
        }
        return mediaItem;
    }   

    private MediaItemDetail toDetail(MediaItem mediaItem) {
        List<Long> tagIds = mediaTagRelationService.listTagIds(mediaItem.getId());
        return new MediaItemDetail(mediaItem, tagIds);
    }

    private void syncTags(long mediaId, MediaItemCommand command) {
        if (!command.hasTagInstruction()) {
            return;
        }
        mediaTagRelationService.replaceTags(mediaId, command.tagIdsOrEmpty());
    }
}
