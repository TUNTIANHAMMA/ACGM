package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.Users;
import com.acgm.acgmmediatracker.mapper.UsersMapper;
import com.acgm.acgmmediatracker.service.UsersService;
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
public class UsersServiceImpl implements UsersService {

    private final UsersMapper usersMapper;

    @Override
    public Users getById(long id) {
        Users user = usersMapper.selectById(id);
        if (user == null) {
            throw new ResourceNotFoundException("未找到指定用户，ID=" + id);
        }
        return user;
    }

    @Override
    public List<Users> listAll() {
        List<Users> users = usersMapper.selectAll();
        return users == null ? Collections.emptyList() : users;
    }

    @Override
    @Transactional
    public Users create(Users user) {
        Objects.requireNonNull(user, "用户实体不能为空");
        usersMapper.insert(user);
        return getById(user.getId());
    }

    @Override
    @Transactional
    public Users update(long id, Users user) {
        Objects.requireNonNull(user, "用户实体不能为空");
        Users existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(user, existing);
        existing.setId(id);
        usersMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        usersMapper.delete(id);
    }
}
