package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.ProgressMusic;
import com.acgm.acgmmediatracker.mapper.ProgressMusicMapper;
import com.acgm.acgmmediatracker.service.ProgressMusicService;
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
public class ProgressMusicServiceImpl implements ProgressMusicService {

    private final ProgressMusicMapper progressMusicMapper;

    @Override
    public ProgressMusic getByMediaId(long mediaId) {
        ProgressMusic progress = progressMusicMapper.selectByMediaId(mediaId);
        if (progress == null) {
            throw new ResourceNotFoundException("未找到音乐进度，mediaId=" + mediaId);
        }
        return progress;
    }

    @Override
    public List<ProgressMusic> listAll() {
        List<ProgressMusic> progresses = progressMusicMapper.selectAll();
        return progresses == null ? Collections.emptyList() : progresses;
    }

    @Override
    @Transactional
    public ProgressMusic create(ProgressMusic progressMusic) {
        Objects.requireNonNull(progressMusic, "音乐进度实体不能为空");
        progressMusicMapper.insert(progressMusic);
        return getByMediaId(progressMusic.getMediaId());
    }

    @Override
    @Transactional
    public ProgressMusic update(long mediaId, ProgressMusic progressMusic) {
        Objects.requireNonNull(progressMusic, "音乐进度实体不能为空");
        ProgressMusic existing = getByMediaId(mediaId);
        ServiceBeanUtils.copyNonNullProperties(progressMusic, existing);
        existing.setMediaId(mediaId);
        progressMusicMapper.update(existing);
        return getByMediaId(mediaId);
    }

    @Override
    @Transactional
    public void delete(long mediaId) {
        getByMediaId(mediaId);
        progressMusicMapper.delete(mediaId);
    }
}
