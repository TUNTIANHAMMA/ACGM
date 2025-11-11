package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.ProgressComic;
import com.acgm.acgmmediatracker.mapper.ProgressComicMapper;
import com.acgm.acgmmediatracker.service.ProgressComicService;
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
public class ProgressComicServiceImpl implements ProgressComicService {

    private final ProgressComicMapper progressComicMapper;

    @Override
    public ProgressComic getByMediaId(long mediaId) {
        ProgressComic progress = progressComicMapper.selectByMediaId(mediaId);
        if (progress == null) {
            throw new ResourceNotFoundException("未找到漫画进度，mediaId=" + mediaId);
        }
        return progress;
    }

    @Override
    public List<ProgressComic> listAll() {
        List<ProgressComic> progresses = progressComicMapper.selectAll();
        return progresses == null ? Collections.emptyList() : progresses;
    }

    @Override
    @Transactional
    public ProgressComic create(ProgressComic progressComic) {
        Objects.requireNonNull(progressComic, "漫画进度实体不能为空");
        progressComicMapper.insert(progressComic);
        return getByMediaId(progressComic.getMediaId());
    }

    @Override
    @Transactional
    public ProgressComic update(long mediaId, ProgressComic progressComic) {
        Objects.requireNonNull(progressComic, "漫画进度实体不能为空");
        ProgressComic existing = getByMediaId(mediaId);
        ServiceBeanUtils.copyNonNullProperties(progressComic, existing);
        existing.setMediaId(mediaId);
        progressComicMapper.update(existing);
        return getByMediaId(mediaId);
    }

    @Override
    @Transactional
    public void delete(long mediaId) {
        getByMediaId(mediaId);
        progressComicMapper.delete(mediaId);
    }
}
