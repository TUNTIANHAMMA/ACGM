package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.ProgressAnime;

import java.util.List;

public interface ProgressAnimeService {

    ProgressAnime getByMediaId(long mediaId);

    List<ProgressAnime> listAll();

    ProgressAnime create(ProgressAnime progressAnime);

    ProgressAnime update(long mediaId, ProgressAnime progressAnime);

    void delete(long mediaId);
}
