package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MediaItemMapper {

    MediaItem selectById(@Param("id") long id);

    List<MediaItem> selectAll();

    int insert(MediaItem mediaItem);

    int update(MediaItem mediaItem);

    int delete(@Param("id") long id);
}
