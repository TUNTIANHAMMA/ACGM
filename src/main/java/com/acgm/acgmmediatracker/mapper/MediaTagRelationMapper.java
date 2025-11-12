package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MediaTagRelationMapper {

    MediaTagRelation selectOne(@Param("mediaId") long mediaId, @Param("tagId") long tagId);

    List<MediaTagRelation> selectByMediaId(@Param("mediaId") long mediaId);

    List<MediaTagRelation> selectByTagId(@Param("tagId") long tagId);

    int insert(MediaTagRelation relation);

    int insertBatch(@Param("mediaId") long mediaId, @Param("tagIds") List<Long> tagIds);

    int delete(@Param("mediaId") long mediaId, @Param("tagId") long tagId);

    int deleteByMediaIdAndTagIds(@Param("mediaId") long mediaId, @Param("tagIds") List<Long> tagIds);
}
