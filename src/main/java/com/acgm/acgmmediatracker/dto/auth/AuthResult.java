package com.acgm.acgmmediatracker.dto.auth;

import com.acgm.acgmmediatracker.entity.User;
import lombok.Getter;

import java.time.Instant;

@Getter
public class AuthResult {

    private final User user;
    private final String token;
    private final Instant issuedAt;

    public AuthResult(User user, String token, Instant issuedAt) {
        this.user = user;
        this.token = token;
        this.issuedAt = issuedAt;
    }
}
