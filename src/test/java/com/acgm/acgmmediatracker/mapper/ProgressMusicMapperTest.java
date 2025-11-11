package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.ProgressMusic;
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
class ProgressMusicMapperTest {

    @Autowired
    private ProgressMusicMapper progressMusicMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndFindProgressMusic() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "music");

        ProgressMusic progress = new ProgressMusic();
        progress.setMediaId(mediaItem.getId());
        progress.setPlayCount(5);
        progress.setListenStatus("listened");
        progress.setUpdatedAt(Timestamp.from(Instant.now()));
        progressMusicMapper.insert(progress);

        ProgressMusic found = progressMusicMapper.selectByMediaId(mediaItem.getId());
        assertThat(found).isNotNull();
        assertThat(found.getPlayCount()).isEqualTo(5);
        assertThat(found.getListenStatus()).isEqualTo("listened");
    }
}
