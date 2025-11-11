package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.dto.auth.AuthResult;
import com.acgm.acgmmediatracker.dto.auth.UserRegistrationRequest;
import com.acgm.acgmmediatracker.entity.User;

public interface AuthService {

    User register(UserRegistrationRequest request);

    AuthResult login(String usernameOrEmail, String rawPassword);
}
