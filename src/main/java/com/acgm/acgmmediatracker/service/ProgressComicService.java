package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.ProgressComic;

import java.util.List;

public interface ProgressComicService {

    ProgressComic getByMediaId(long mediaId);

    List<ProgressComic> listAll();

    ProgressComic create(ProgressComic progressComic);

    ProgressComic update(long mediaId, ProgressComic progressComic);

    void delete(long mediaId);
}
