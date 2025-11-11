package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.Favorites;

import java.util.List;

public interface FavoritesService {

    Favorites getById(long id);

    List<Favorites> listAll();

    Favorites create(Favorites favorite);

    Favorites update(long id, Favorites favorite);

    void delete(long id);
}
