package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.MediaApiInfo;
import com.acgm.acgmmediatracker.mapper.MediaApiInfoMapper;
import com.acgm.acgmmediatracker.service.MediaApiInfoService;
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
public class MediaApiInfoServiceImpl implements MediaApiInfoService {

    private final MediaApiInfoMapper mediaApiInfoMapper;

    @Override
    public MediaApiInfo getById(long id) {
        MediaApiInfo info = mediaApiInfoMapper.selectById(id);
        if (info == null) {
            throw new ResourceNotFoundException("未找到媒体 API 信息，ID=" + id);
        }
        return info;
    }

    @Override
    public List<MediaApiInfo> listAll() {
        List<MediaApiInfo> list = mediaApiInfoMapper.selectAll();
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    @Transactional
    public MediaApiInfo create(MediaApiInfo mediaApiInfo) {
        Objects.requireNonNull(mediaApiInfo, "媒体 API 信息实体不能为空");
        mediaApiInfoMapper.insert(mediaApiInfo);
        return getById(mediaApiInfo.getId());
    }

    @Override
    @Transactional
    public MediaApiInfo update(long id, MediaApiInfo mediaApiInfo) {
        Objects.requireNonNull(mediaApiInfo, "媒体 API 信息实体不能为空");
        MediaApiInfo existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(mediaApiInfo, existing);
        existing.setId(id);
        mediaApiInfoMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        mediaApiInfoMapper.delete(id);
    }
}
