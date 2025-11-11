package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.MediaItem;

import java.util.List;

public interface MediaItemService {

    MediaItem getById(long id);

    List<MediaItem> listAll();

    MediaItem create(MediaItem mediaItem);

    MediaItem update(long id, MediaItem mediaItem);

    void delete(long id);
}
