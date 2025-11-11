package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaApiInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MediaApiInfoMapper {

    MediaApiInfo selectById(@Param("id") long id);

    List<MediaApiInfo> selectAll();

    int insert(MediaApiInfo mediaApiInfo);

    int update(MediaApiInfo mediaApiInfo);

    int delete(@Param("id") long id);
}
