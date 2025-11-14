package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.MediaLibrary;
import com.acgm.acgmmediatracker.entity.Tag;
import com.acgm.acgmmediatracker.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MapperTestHelper {

    private final UserMapper userMapper;
    private final MediaItemMapper mediaItemMapper;
    private final MediaLibraryMapper mediaLibraryMapper;
    private final TagMapper tagMapper;

//    public MapperTestHelper(UserMapper userMapper,
//                            MediaItemMapper mediaItemMapper,
//                            TagMapper tagMapper) {
//        this.userMapper = userMapper;
//        this.mediaItemMapper = mediaItemMapper;
//        this.tagMapper = tagMapper;
//    }

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

    public MediaLibrary createMediaLibrary(String type) {
        MediaLibrary library = new MediaLibrary();
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        library.setType(type);
        library.setTitle("library-" + suffix);
        library.setOriginalTitle("orig-" + suffix);
        library.setYear(2020);
        library.setCoverUrl("http://example.com/lib/" + suffix);
        library.setSource("manual");
        library.setMeta("{\"from\":\"helper\"}");
        Timestamp now = Timestamp.from(Instant.now());
        library.setCreatedAt(now);
        library.setUpdatedAt(now);
        mediaLibraryMapper.insert(library);
        return library;
    }

    public MediaItem createMediaItem(long userId, String type) {
        MediaLibrary library = createMediaLibrary(type);
        return createMediaItem(userId, library);
    }

    public MediaItem createMediaItem(long userId, MediaLibrary library) {
        return createMediaItem(userId, library.getId());
    }

    private MediaItem createMediaItem(long userId, long libraryId) {
        MediaItem item = new MediaItem();
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        item.setUserId(userId);
        item.setLibraryId(libraryId);
        item.setStatus("planned");
        item.setCustomTitle("media-" + suffix);
        item.setNotes("notes");
        item.setRating(8.5);
        LocalDate today = LocalDate.now();
        item.setStartDate(Date.valueOf(today));
        item.setFinishDate(Date.valueOf(today));
        item.setCustomCoverUrl("http://example.com/" + suffix);
        item.setCustomSource("manual");
        item.setDeletedAt(null);
        item.setRowVersion(0L);
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
