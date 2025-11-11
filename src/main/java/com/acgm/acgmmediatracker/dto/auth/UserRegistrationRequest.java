package com.acgm.acgmmediatracker.dto.auth;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String username;
    private String email;
    private String password;
    private String role;
    private String preference;
}
