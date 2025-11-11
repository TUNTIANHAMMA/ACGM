package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Reviews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewsMapper {

    Reviews selectById(@Param("id") long id);

    List<Reviews> selectAll();

    int insert(Reviews reviews);

    int update(Reviews reviews);

    int delete(@Param("id") long id);
}
