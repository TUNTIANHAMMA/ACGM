package com.acgm.acgmmediatracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 定义 Spring Security 相关的基础 Bean，决定 HTTP 过滤链和密码加密策略。
 * 目前项目处于开放阶段，所有请求放行，同时提供 BCrypt 加密器供注册/登录复用。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 构建当前应用的安全过滤链：关闭 CSRF、放行所有请求并启用最基础的 HTTP Basic 配置。
     * 这里保留了扩展点，未来可以在 authorizeHttpRequests 中增加更细粒度的规则。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // REST 场景下不用表单，直接关闭 CSRF 校验。
            .authorizeHttpRequests(registry -> registry.anyRequest().permitAll()) // 暂时放行全部请求。
            .httpBasic(Customizer.withDefaults()); // 开启默认的 Basic 认证以便调试。
        return http.build();
    }

    /**
     * 提供一个全局可注入的 BCrypt 密码加密器，用于安全地存储用户密码。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
