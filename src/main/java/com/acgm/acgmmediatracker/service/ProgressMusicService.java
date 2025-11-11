package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.ProgressMusic;

import java.util.List;

public interface ProgressMusicService {

    ProgressMusic getByMediaId(long mediaId);

    List<ProgressMusic> listAll();

    ProgressMusic create(ProgressMusic progressMusic);

    ProgressMusic update(long mediaId, ProgressMusic progressMusic);

    void delete(long mediaId);
}
