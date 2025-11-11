package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.ProgressAnime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgressAnimeMapper {

    ProgressAnime selectByMediaId(@Param("mediaId") long mediaId);

    List<ProgressAnime> selectAll();

    int insert(ProgressAnime progressAnime);

    int update(ProgressAnime progressAnime);

    int delete(@Param("mediaId") long mediaId);
}
