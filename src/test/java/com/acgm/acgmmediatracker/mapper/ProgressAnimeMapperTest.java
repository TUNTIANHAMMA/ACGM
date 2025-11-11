package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.ProgressAnime;
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
class ProgressAnimeMapperTest {

    @Autowired
    private ProgressAnimeMapper progressAnimeMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndFindProgressAnime() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "anime");

        ProgressAnime progress = new ProgressAnime();
        progress.setMediaId(mediaItem.getId());
        progress.setCurrentEpisode(3);
        progress.setTotalEpisodes(12);
        progress.setUpdatedAt(Timestamp.from(Instant.now()));
        progressAnimeMapper.insert(progress);

        ProgressAnime found = progressAnimeMapper.selectByMediaId(mediaItem.getId());
        assertThat(found).isNotNull();
        assertThat(found.getCurrentEpisode()).isEqualTo(3);
        assertThat(found.getTotalEpisodes()).isEqualTo(12);
    }
}
