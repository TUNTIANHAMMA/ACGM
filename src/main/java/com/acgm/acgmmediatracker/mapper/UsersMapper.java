package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersMapper {

    Users selectById(@Param("id") long id);

    List<Users> selectAll();

    int insert(Users user);

    int update(Users user);

    int delete(@Param("id") long id);
}
