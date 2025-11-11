package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.ProgressGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgressGameMapper {

    ProgressGame selectByMediaId(@Param("mediaId") long mediaId);

    List<ProgressGame> selectAll();

    int insert(ProgressGame progressGame);

    int update(ProgressGame progressGame);

    int delete(@Param("mediaId") long mediaId);
}
