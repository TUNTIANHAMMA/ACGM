package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.Users;

import java.util.List;

public interface UsersService {

    Users getById(long id);

    List<Users> listAll();

    Users create(Users user);

    Users update(long id, Users user);

    void delete(long id);
}
