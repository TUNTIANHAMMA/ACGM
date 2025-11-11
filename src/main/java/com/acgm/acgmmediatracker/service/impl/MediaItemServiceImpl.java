package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.mapper.MediaItemMapper;
import com.acgm.acgmmediatracker.service.MediaItemService;
import com.acgm.acgmmediatracker.service.exception.ResourceNotFoundException;
import com.acgm.acgmmediatracker.service.util.ServiceBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaItemServiceImpl implements MediaItemService {

    private final MediaItemMapper mediaItemMapper;

    @Override
    public MediaItem getById(long id) {
        MediaItem mediaItem = mediaItemMapper.selectById(id);
        if (mediaItem == null) {
            throw new ResourceNotFoundException("未找到媒体条目，ID=" + id);
        }
        return mediaItem;
    }

    @Override
    public List<MediaItem> listAll() {
        List<MediaItem> items = mediaItemMapper.selectAll();
        return items == null ? Collections.emptyList() : items;
    }

    @Override
    @Transactional
    public MediaItem create(MediaItem mediaItem) {
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");
        mediaItemMapper.insert(mediaItem);
        return getById(mediaItem.getId());
    }

    @Override
    @Transactional
    public MediaItem update(long id, MediaItem mediaItem) {
        Objects.requireNonNull(mediaItem, "媒体条目实体不能为空");
        MediaItem existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(mediaItem, existing);
        existing.setId(id);
        mediaItemMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        mediaItemMapper.delete(id);
    }
}
