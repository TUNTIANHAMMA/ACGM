package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.User;
import com.acgm.acgmmediatracker.mapper.UserMapper;
import com.acgm.acgmmediatracker.service.UserService;
import com.acgm.acgmmediatracker.service.exception.ResourceNotFoundException;
import com.acgm.acgmmediatracker.service.util.ServiceBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User getById(long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ResourceNotFoundException("未找到指定用户，ID=" + id);
        }
        return user;
    }

    @Override
    public List<User> listAll() {
        List<User> users = userMapper.selectAll();
        return users == null ? Collections.emptyList() : users;
    }

    @Override
    @Transactional
    public User create(User user) {
        Objects.requireNonNull(user, "用户实体不能为空");
        userMapper.insert(user);
        return getById(user.getId());
    }

    @Override
    @Transactional
    public User update(long id, User user) {
        Objects.requireNonNull(user, "用户实体不能为空");
        User existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(user, existing);
        existing.setId(id);
        userMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        userMapper.delete(id);
    }
}
