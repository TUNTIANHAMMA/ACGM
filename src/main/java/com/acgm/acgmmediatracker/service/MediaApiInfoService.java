package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.MediaApiInfo;

import java.util.List;

public interface MediaApiInfoService {

    MediaApiInfo getById(long id);

    List<MediaApiInfo> listAll();

    MediaApiInfo create(MediaApiInfo mediaApiInfo);

    MediaApiInfo update(long id, MediaApiInfo mediaApiInfo);

    void delete(long id);
}
