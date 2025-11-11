package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.ProgressGame;

import java.util.List;

public interface ProgressGameService {

    ProgressGame getByMediaId(long mediaId);

    List<ProgressGame> listAll();

    ProgressGame create(ProgressGame progressGame);

    ProgressGame update(long mediaId, ProgressGame progressGame);

    void delete(long mediaId);
}
