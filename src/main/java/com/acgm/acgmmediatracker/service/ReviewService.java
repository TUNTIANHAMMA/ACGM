package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.Review;

import java.util.List;

public interface ReviewService {

    Review getById(long id);

    List<Review> listAll();

    Review create(Review review);

    Review update(long id, Review review);

    void delete(long id);
}
