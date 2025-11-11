package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {

    Tag selectById(@Param("id") long id);

    List<Tag> selectAll();

    int insert(Tag tag);

    int update(Tag tag);

    int delete(@Param("id") long id);
}
