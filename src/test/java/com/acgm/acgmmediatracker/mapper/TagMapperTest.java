package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Tag;
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
class TagMapperTest {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndSelectTag() {
        User user = helper.createUser();
        Tag tag = new Tag();
        tag.setUserId(user.getId());
        tag.setName("tag-test");
        tag.setCreatedAt(Timestamp.from(Instant.now()));
        tagMapper.insert(tag);

        Tag found = tagMapper.selectById(tag.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("tag-test");
    }
}
