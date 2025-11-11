package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MediaItemsMapper {

    MediaItems selectById(@Param("id") long id);

    List<MediaItems> selectAll();

    int insert(MediaItems mediaItems);

    int update(MediaItems mediaItems);

    int delete(@Param("id") long id);
}
