package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(@Param("id") long id);

    User selectByUsername(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    List<User> selectAll();

    int insert(User user);

    int update(User user);

    int delete(@Param("id") long id);
}
