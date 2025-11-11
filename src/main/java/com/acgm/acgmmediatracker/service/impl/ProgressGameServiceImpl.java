package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.ProgressGame;
import com.acgm.acgmmediatracker.mapper.ProgressGameMapper;
import com.acgm.acgmmediatracker.service.ProgressGameService;
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
public class ProgressGameServiceImpl implements ProgressGameService {

    private final ProgressGameMapper progressGameMapper;

    @Override
    public ProgressGame getByMediaId(long mediaId) {
        ProgressGame progress = progressGameMapper.selectByMediaId(mediaId);
        if (progress == null) {
            throw new ResourceNotFoundException("未找到游戏进度，mediaId=" + mediaId);
        }
        return progress;
    }

    @Override
    public List<ProgressGame> listAll() {
        List<ProgressGame> progresses = progressGameMapper.selectAll();
        return progresses == null ? Collections.emptyList() : progresses;
    }

    @Override
    @Transactional
    public ProgressGame create(ProgressGame progressGame) {
        Objects.requireNonNull(progressGame, "游戏进度实体不能为空");
        progressGameMapper.insert(progressGame);
        return getByMediaId(progressGame.getMediaId());
    }

    @Override
    @Transactional
    public ProgressGame update(long mediaId, ProgressGame progressGame) {
        Objects.requireNonNull(progressGame, "游戏进度实体不能为空");
        ProgressGame existing = getByMediaId(mediaId);
        ServiceBeanUtils.copyNonNullProperties(progressGame, existing);
        existing.setMediaId(mediaId);
        progressGameMapper.update(existing);
        return getByMediaId(mediaId);
    }

    @Override
    @Transactional
    public void delete(long mediaId) {
        getByMediaId(mediaId);
        progressGameMapper.delete(mediaId);
    }
}
