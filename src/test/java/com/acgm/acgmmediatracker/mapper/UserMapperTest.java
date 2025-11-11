package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void selectByIdReturnsInsertedUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setEmail("user@example.com");
        user.setEmailNorm("user@example.com");
        user.setPassword("secret");
        user.setRole("USER");
        Timestamp now = Timestamp.from(Instant.now());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userMapper.insert(user);

        User found = userMapper.selectById(user.getId());
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("test-user");
        assertThat(found.getEmail()).isEqualTo("user@example.com");
    }
}
