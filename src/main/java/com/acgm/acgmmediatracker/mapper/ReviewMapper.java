package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    Review selectById(@Param("id") long id);

    List<Review> selectAll();

    int insert(Review review);

    int update(Review review);

    int delete(@Param("id") long id);
}
