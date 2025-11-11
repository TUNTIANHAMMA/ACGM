package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Favorites;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoritesMapper {

    Favorites selectById(@Param("id") long id);

    List<Favorites> selectAll();

    int insert(Favorites favorite);

    int update(Favorites favorite);

    int delete(@Param("id") long id);
}
