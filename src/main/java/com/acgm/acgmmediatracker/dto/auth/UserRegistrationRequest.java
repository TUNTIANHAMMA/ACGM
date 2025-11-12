package com.acgm.acgmmediatracker.dto.auth;

import lombok.Data;

/**
 * 承载注册接口提交的数据，供控制层与服务层之间传递使用。
 */
@Data
public class UserRegistrationRequest {

    private String username;
    private String email;
    private String password;
    private String role;
    private String preference;
}
