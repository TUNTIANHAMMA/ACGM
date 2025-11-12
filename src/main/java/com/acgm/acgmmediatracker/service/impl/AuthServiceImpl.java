package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.dto.auth.AuthResult;
import com.acgm.acgmmediatracker.dto.auth.UserRegistrationRequest;
import com.acgm.acgmmediatracker.entity.User;
import com.acgm.acgmmediatracker.mapper.UserMapper;
import com.acgm.acgmmediatracker.service.AuthService;
import com.acgm.acgmmediatracker.service.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(UserRegistrationRequest request) {
        Objects.requireNonNull(request, "注册请求不能为空");
        validateRegistration(request);
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPreference(request.getPreference());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // INSERT 语句中明确写出了字段名 role，MySQL 就会 忽略默认值 DEFAULT 'user'。所以需要进行判断。
        user.setRole(request.getRole() == null ? "user" : request.getRole());
        Timestamp now = Timestamp.from(Instant.now());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userMapper.insert(user);
        // 再次从数据库查询，确保返回的是包含数据库自动生成字段（如id、默认值、时间戳等）的完整用户数据
        return userMapper.selectById(user.getId());
    }

    @Override
    public AuthResult login(String usernameOrEmail, String rawPassword) {
        Objects.requireNonNull(usernameOrEmail, "用户名或邮箱不能为空");
        Objects.requireNonNull(rawPassword, "密码不能为空");
        User user = resolveUser(usernameOrEmail);
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthException("用户名或密码错误");
        }
        String token = UUID.randomUUID().toString();
        return new AuthResult(user, token, Instant.now());
    }


    private void validateRegistration(UserRegistrationRequest request) {
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new AuthException("用户名已存在");
        }
        if (userMapper.selectByEmail(request.getEmail()) != null) {
            throw new AuthException("邮箱已存在");
        }
    }

    private User resolveUser(String usernameOrEmail) {
        User user = userMapper.selectByUsername(usernameOrEmail);
        if (user == null) {
            user = userMapper.selectByEmail(usernameOrEmail);
        }
        if (user == null) {
            throw new AuthException("用户不存在");
        }
        return user;
    }
}
