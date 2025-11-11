package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.Favorite;
import com.acgm.acgmmediatracker.entity.MediaItem;
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
class FavoriteMapperTest {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndSelectFavorite() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "anime");

        Favorite favorite = new Favorite();
        favorite.setUserId(user.getId());
        favorite.setMediaId(mediaItem.getId());
        favorite.setCreatedAt(Timestamp.from(Instant.now()));
        favoriteMapper.insert(favorite);

        Favorite found = favoriteMapper.selectById(favorite.getId());
        assertThat(found).isNotNull();
        assertThat(found.getUserId()).isEqualTo(user.getId());
        assertThat(found.getMediaId()).isEqualTo(mediaItem.getId());
    }
}
