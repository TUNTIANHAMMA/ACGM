package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.MediaTagRelation;
import com.acgm.acgmmediatracker.mapper.MediaTagRelationMapper;
import com.acgm.acgmmediatracker.service.MediaTagRelationService;
import com.acgm.acgmmediatracker.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaTagRelationServiceImpl implements MediaTagRelationService {

    private final MediaTagRelationMapper relationMapper;

    @Override
    public MediaTagRelation get(long mediaId, long tagId) {
        MediaTagRelation relation = relationMapper.selectOne(mediaId, tagId);
        if (relation == null) {
            throw new ResourceNotFoundException("未找到媒体与标签关系，mediaId=" + mediaId + ", tagId=" + tagId);
        }
        return relation;
    }

    @Override
    public List<MediaTagRelation> listByMediaId(long mediaId) {
        List<MediaTagRelation> relations = relationMapper.selectByMediaId(mediaId);
        return relations == null ? Collections.emptyList() : relations;
    }

    @Override
    public List<Long> listTagIds(long mediaId) {
        return listByMediaId(mediaId)
                .stream()
                .map(MediaTagRelation::getTagId)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaTagRelation> listByTagId(long tagId) {
        List<MediaTagRelation> relations = relationMapper.selectByTagId(tagId);
        return relations == null ? Collections.emptyList() : relations;
    }

    @Override
    @Transactional
    public MediaTagRelation create(MediaTagRelation relation) {
        Objects.requireNonNull(relation, "媒体标签关系不能为空");
        relationMapper.insert(relation);
        return get(relation.getMediaId(), relation.getTagId());
    }

    @Override
    @Transactional
    public void replaceTags(long mediaId, List<Long> tagIds) {
        LinkedHashSet<Long> desired = tagIds == null ? new LinkedHashSet<>() : tagIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Long> existing = listByMediaId(mediaId).stream()
                .map(MediaTagRelation::getTagId)
                .collect(Collectors.toSet());

        List<Long> toAdd = desired.stream()
                .filter(id -> !existing.contains(id))
                .collect(Collectors.toList());

        List<Long> toRemove = existing.stream()
                .filter(id -> !desired.contains(id))
                .collect(Collectors.toList());

        if (!toAdd.isEmpty()) {
            relationMapper.insertBatch(mediaId, toAdd);
        }
        if (!toRemove.isEmpty()) {
            relationMapper.deleteByMediaIdAndTagIds(mediaId, toRemove);
        }
    }

    @Override
    @Transactional
    public void delete(long mediaId, long tagId) {
        get(mediaId, tagId);
        relationMapper.delete(mediaId, tagId);
    }
}
