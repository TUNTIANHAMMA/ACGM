package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MediaItemMapperTest {

    @Autowired
    private MediaItemMapper mediaItemMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndSelectMediaItem() {
        User user = helper.createUser();
        MediaItem item = helper.createMediaItem(user.getId(), "anime");

        MediaItem found = mediaItemMapper.selectById(item.getId());
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo(item.getTitle());
        assertThat(found.getUserId()).isEqualTo(user.getId());
    }
}
