package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.Tag;
import com.acgm.acgmmediatracker.entity.User;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class MapperTestHelper {

    private final UserMapper userMapper;
    private final MediaItemMapper mediaItemMapper;
    private final TagMapper tagMapper;

    public MapperTestHelper(UserMapper userMapper,
                            MediaItemMapper mediaItemMapper,
                            TagMapper tagMapper) {
        this.userMapper = userMapper;
        this.mediaItemMapper = mediaItemMapper;
        this.tagMapper = tagMapper;
    }

    public User createUser() {
        User user = new User();
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        user.setUsername("user-" + suffix);
        user.setEmail("user-" + suffix + "@example.com");
        user.setPreference("{\"theme\":\"dark\"}");
        user.setPassword("pwd-" + suffix);
        user.setRole("user");
        Timestamp now = Timestamp.from(Instant.now());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userMapper.insert(user);
        return user;
    }

    public MediaItem createMediaItem(long userId, String type) {
        MediaItem item = new MediaItem();
        item.setUserId(userId);
        item.setType(type);
        item.setStatus("planned");
        item.setTitle("media-" + UUID.randomUUID().toString().substring(0, 8));
        item.setNotes("notes");
        item.setRating(8.5);
        LocalDate today = LocalDate.now();
        item.setStartDate(Date.valueOf(today));
        item.setFinishDate(Date.valueOf(today));
        item.setCoverUrl("http://example.com/" + UUID.randomUUID());
        item.setSource("manual");
        Timestamp now = Timestamp.from(Instant.now());
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        item.setDeletedAt(null);
        mediaItemMapper.insert(item);
        return item;
    }

    public Tag createTag(long userId) {
        Tag tag = new Tag();
        tag.setUserId(userId);
        tag.setName("tag-" + UUID.randomUUID().toString().substring(0, 8));
        tag.setCreatedAt(Timestamp.from(Instant.now()));
        tagMapper.insert(tag);
        return tag;
    }
}
