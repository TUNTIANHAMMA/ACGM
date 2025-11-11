package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    Favorite selectById(@Param("id") long id);

    List<Favorite> selectAll();

    int insert(Favorite favorite);

    int update(Favorite favorite);

    int delete(@Param("id") long id);
}
