package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.Reviews;
import com.acgm.acgmmediatracker.mapper.ReviewsMapper;
import com.acgm.acgmmediatracker.service.ReviewsService;
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
public class ReviewsServiceImpl implements ReviewsService {

    private final ReviewsMapper reviewsMapper;

    @Override
    public Reviews getById(long id) {
        Reviews review = reviewsMapper.selectById(id);
        if (review == null) {
            throw new ResourceNotFoundException("未找到点评记录，ID=" + id);
        }
        return review;
    }

    @Override
    public List<Reviews> listAll() {
        List<Reviews> reviews = reviewsMapper.selectAll();
        return reviews == null ? Collections.emptyList() : reviews;
    }

    @Override
    @Transactional
    public Reviews create(Reviews reviews) {
        Objects.requireNonNull(reviews, "点评实体不能为空");
        reviewsMapper.insert(reviews);
        return getById(reviews.getId());
    }

    @Override
    @Transactional
    public Reviews update(long id, Reviews reviews) {
        Objects.requireNonNull(reviews, "点评实体不能为空");
        Reviews existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(reviews, existing);
        existing.setId(id);
        reviewsMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        reviewsMapper.delete(id);
    }
}
