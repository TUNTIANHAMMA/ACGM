package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.User;

import java.util.List;

public interface UserService {

    User getById(long id);

    List<User> listAll();

    User create(User user);

    User update(long id, User user);

    void delete(long id);
}
