package com.acgm.acgmmediatracker.dto.auth;

import com.acgm.acgmmediatracker.entity.User;
import lombok.Getter;

import java.time.Instant;

/**
 * 封装一次登录或认证成功后返回给客户端的核心信息。
 * 包含完整的用户实体、颁发给客户端的 token 以及 token 的签发时间。
 */
@Getter
public class AuthResult {

    private final User user;
    private final String token;
    private final Instant issuedAt;

    /**
     * 构造一条认证结果记录，便于调用方一次性拿到用户、token 与签发时间。
     *
     * @param user     成功认证的用户
     * @param token    返回给客户端用于后续请求的 token
     * @param issuedAt token 的签发时间，可用于失效或刷新判断
     */
    public AuthResult(User user, String token, Instant issuedAt) {
        this.user = user;
        this.token = token;
        this.issuedAt = issuedAt;
    }
}
