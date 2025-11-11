package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite getById(long id);

    List<Favorite> listAll();

    Favorite create(Favorite favorite);

    Favorite update(long id, Favorite favorite);

    void delete(long id);
}
