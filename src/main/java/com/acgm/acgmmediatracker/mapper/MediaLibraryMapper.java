package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MediaLibraryMapper {

    MediaLibrary selectById(@Param("id") long id);

    List<MediaLibrary> selectAll();

    int insert(MediaLibrary mediaLibrary);

    int update(MediaLibrary mediaLibrary);

    int delete(@Param("id") long id);
}
