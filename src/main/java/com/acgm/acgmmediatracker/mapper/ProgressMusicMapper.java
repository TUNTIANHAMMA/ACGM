package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.ProgressMusic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgressMusicMapper {

    ProgressMusic selectByMediaId(@Param("mediaId") long mediaId);

    List<ProgressMusic> selectAll();

    int insert(ProgressMusic progressMusic);

    int update(ProgressMusic progressMusic);

    int delete(@Param("mediaId") long mediaId);
}
