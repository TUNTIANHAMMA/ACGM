package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.MediaItems;
import com.acgm.acgmmediatracker.mapper.MediaItemsMapper;
import com.acgm.acgmmediatracker.service.MediaItemsService;
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
public class MediaItemsServiceImpl implements MediaItemsService {

    private final MediaItemsMapper mediaItemsMapper;

    @Override
    public MediaItems getById(long id) {
        MediaItems mediaItems = mediaItemsMapper.selectById(id);
        if (mediaItems == null) {
            throw new ResourceNotFoundException("未找到媒体条目，ID=" + id);
        }
        return mediaItems;
    }

    @Override
    public List<MediaItems> listAll() {
        List<MediaItems> items = mediaItemsMapper.selectAll();
        return items == null ? Collections.emptyList() : items;
    }

    @Override
    @Transactional
    public MediaItems create(MediaItems mediaItems) {
        Objects.requireNonNull(mediaItems, "媒体条目实体不能为空");
        mediaItemsMapper.insert(mediaItems);
        return getById(mediaItems.getId());
    }

    @Override
    @Transactional
    public MediaItems update(long id, MediaItems mediaItems) {
        Objects.requireNonNull(mediaItems, "媒体条目实体不能为空");
        MediaItems existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(mediaItems, existing);
        existing.setId(id);
        mediaItemsMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        mediaItemsMapper.delete(id);
    }
}
