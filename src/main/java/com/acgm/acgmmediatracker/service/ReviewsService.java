package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.Reviews;

import java.util.List;

public interface ReviewsService {

    Reviews getById(long id);

    List<Reviews> listAll();

    Reviews create(Reviews reviews);

    Reviews update(long id, Reviews reviews);

    void delete(long id);
}
