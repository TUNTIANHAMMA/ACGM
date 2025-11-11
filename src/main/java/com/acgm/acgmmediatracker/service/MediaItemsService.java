package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.MediaItems;

import java.util.List;

public interface MediaItemsService {

    MediaItems getById(long id);

    List<MediaItems> listAll();

    MediaItems create(MediaItems mediaItems);

    MediaItems update(long id, MediaItems mediaItems);

    void delete(long id);
}
