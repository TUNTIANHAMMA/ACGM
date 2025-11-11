package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.ProgressAnime;
import com.acgm.acgmmediatracker.mapper.ProgressAnimeMapper;
import com.acgm.acgmmediatracker.service.ProgressAnimeService;
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
public class ProgressAnimeServiceImpl implements ProgressAnimeService {

    private final ProgressAnimeMapper progressAnimeMapper;

    @Override
    public ProgressAnime getByMediaId(long mediaId) {
        ProgressAnime progress = progressAnimeMapper.selectByMediaId(mediaId);
        if (progress == null) {
            throw new ResourceNotFoundException("未找到动漫进度，mediaId=" + mediaId);
        }
        return progress;
    }

    @Override
    public List<ProgressAnime> listAll() {
        List<ProgressAnime> progresses = progressAnimeMapper.selectAll();
        return progresses == null ? Collections.emptyList() : progresses;
    }

    @Override
    @Transactional
    public ProgressAnime create(ProgressAnime progressAnime) {
        Objects.requireNonNull(progressAnime, "动漫进度实体不能为空");
        progressAnimeMapper.insert(progressAnime);
        return getByMediaId(progressAnime.getMediaId());
    }

    @Override
    @Transactional
    public ProgressAnime update(long mediaId, ProgressAnime progressAnime) {
        Objects.requireNonNull(progressAnime, "动漫进度实体不能为空");
        ProgressAnime existing = getByMediaId(mediaId);
        ServiceBeanUtils.copyNonNullProperties(progressAnime, existing);
        existing.setMediaId(mediaId);
        progressAnimeMapper.update(existing);
        return getByMediaId(mediaId);
    }

    @Override
    @Transactional
    public void delete(long mediaId) {
        getByMediaId(mediaId);
        progressAnimeMapper.delete(mediaId);
    }
}
