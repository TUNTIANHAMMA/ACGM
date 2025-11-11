package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.ProgressComic;
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
class ProgressComicMapperTest {

    @Autowired
    private ProgressComicMapper progressComicMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndFindProgressComic() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "comic");

        ProgressComic progress = new ProgressComic();
        progress.setMediaId(mediaItem.getId());
        progress.setCurrentChapter(10);
        progress.setTotalChapters(100L);
        progress.setCurrentVolume(5L);
        progress.setUpdatedAt(Timestamp.from(Instant.now()));
        progressComicMapper.insert(progress);

        ProgressComic found = progressComicMapper.selectByMediaId(mediaItem.getId());
        assertThat(found).isNotNull();
        assertThat(found.getCurrentChapter()).isEqualTo(10);
        assertThat(found.getCurrentVolume()).isEqualTo(5);
    }
}
