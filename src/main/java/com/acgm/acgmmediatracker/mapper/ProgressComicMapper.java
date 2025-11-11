package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.ProgressComic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgressComicMapper {

    ProgressComic selectByMediaId(@Param("mediaId") long mediaId);

    List<ProgressComic> selectAll();

    int insert(ProgressComic progressComic);

    int update(ProgressComic progressComic);

    int delete(@Param("mediaId") long mediaId);
}
