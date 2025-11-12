package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.MediaTagRelation;

import java.util.List;

public interface MediaTagRelationService {

    MediaTagRelation get(long mediaId, long tagId);

    List<MediaTagRelation> listByMediaId(long mediaId);

    List<Long> listTagIds(long mediaId);

    List<MediaTagRelation> listByTagId(long tagId);

    MediaTagRelation create(MediaTagRelation relation);

    void replaceTags(long mediaId, List<Long> tagIds);

    void delete(long mediaId, long tagId);
}
