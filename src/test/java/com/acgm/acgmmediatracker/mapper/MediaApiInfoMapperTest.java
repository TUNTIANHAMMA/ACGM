package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaApiInfo;
import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MediaApiInfoMapperTest {

    @Autowired
    private MediaApiInfoMapper mediaApiInfoMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndSelectMediaApiInfo() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "anime");

        MediaApiInfo info = new MediaApiInfo();
        info.setMediaId(mediaItem.getId());
        info.setApiSource("bangumi");
        info.setApiId("BG-" + UUID.randomUUID());
        info.setPayload("{\"title\":\"demo\"}");
        info.setLastSync(Timestamp.from(Instant.now()));
        mediaApiInfoMapper.insert(info);

        MediaApiInfo found = mediaApiInfoMapper.selectById(info.getId());
        assertThat(found).isNotNull();
        assertThat(found.getMediaId()).isEqualTo(mediaItem.getId());
        assertThat(found.getApiSource()).isEqualTo("bangumi");
    }
}
