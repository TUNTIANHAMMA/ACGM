package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Users;
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
class UsersMapperTest {

    @Autowired
    private UsersMapper usersMapper;

    @Test
    void selectByIdReturnsInsertedUser() {
        Users user = new Users();
        user.setUsername("test-user");
        user.setEmail("user@example.com");
        user.setEmailNorm("user@example.com");
        user.setPassword("secret");
        user.setRole("USER");
        Timestamp now = Timestamp.from(Instant.now());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        usersMapper.insert(user);

        Users found = usersMapper.selectById(user.getId());
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("test-user");
        assertThat(found.getEmail()).isEqualTo("user@example.com");
    }
}
