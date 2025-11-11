package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.Review;
import com.acgm.acgmmediatracker.mapper.ReviewMapper;
import com.acgm.acgmmediatracker.service.ReviewService;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    public Review getById(long id) {
        Review review = reviewMapper.selectById(id);
        if (review == null) {
            throw new ResourceNotFoundException("未找到点评记录，ID=" + id);
        }
        return review;
    }

    @Override
    public List<Review> listAll() {
        List<Review> reviews = reviewMapper.selectAll();
        return reviews == null ? Collections.emptyList() : reviews;
    }

    @Override
    @Transactional
    public Review create(Review review) {
        Objects.requireNonNull(review, "点评实体不能为空");
        reviewMapper.insert(review);
        return getById(review.getId());
    }

    @Override
    @Transactional
    public Review update(long id, Review review) {
        Objects.requireNonNull(review, "点评实体不能为空");
        Review existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(review, existing);
        existing.setId(id);
        reviewMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        reviewMapper.delete(id);
    }
}
