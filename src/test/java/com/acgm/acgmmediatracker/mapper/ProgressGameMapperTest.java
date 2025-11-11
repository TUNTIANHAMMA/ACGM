package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.ProgressGame;
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
class ProgressGameMapperTest {

    @Autowired
    private ProgressGameMapper progressGameMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndFindProgressGame() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "game");

        ProgressGame progress = new ProgressGame();
        progress.setMediaId(mediaItem.getId());
        progress.setPlatform("PC");
        progress.setCompletionPercent(50);
        progress.setPlayTimeHours(20);
        progress.setUpdatedAt(Timestamp.from(Instant.now()));
        progressGameMapper.insert(progress);

        ProgressGame found = progressGameMapper.selectByMediaId(mediaItem.getId());
        assertThat(found).isNotNull();
        assertThat(found.getPlatform()).isEqualTo("PC");
        assertThat(found.getCompletionPercent()).isEqualTo(50);
    }
}
