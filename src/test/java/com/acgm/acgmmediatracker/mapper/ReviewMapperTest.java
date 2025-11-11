package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.Review;
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
class ReviewMapperTest {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndSelectReview() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "anime");

        Review review = new Review();
        review.setUserId(user.getId());
        review.setMediaId(mediaItem.getId());
        review.setRating(9.0);
        review.setContent("Great show!");
        review.setCreatedAt(Timestamp.from(Instant.now()));
        reviewMapper.insert(review);

        Review found = reviewMapper.selectById(review.getId());
        assertThat(found).isNotNull();
        assertThat(found.getRating()).isEqualTo(9.0);
        assertThat(found.getContent()).contains("Great");
    }
}
