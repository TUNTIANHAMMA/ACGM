package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.dto.media.MediaItemCommand;
import com.acgm.acgmmediatracker.dto.media.MediaItemDetail;
import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.MediaLibrary;
import com.acgm.acgmmediatracker.mapper.MediaItemMapper;
import com.acgm.acgmmediatracker.mapper.MediaLibraryMapper;
import com.acgm.acgmmediatracker.service.impl.MediaItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaItemServiceImplTest {

    @Mock
    private MediaItemMapper mediaItemMapper;

    @Mock
    private MediaLibraryMapper mediaLibraryMapper;

    @Mock
    private MediaTagRelationService mediaTagRelationService;

    @InjectMocks
    private MediaItemServiceImpl mediaItemService;

    @Test
    void createWithTagsPersistsRelationsAndReturnsDetail() {
        MediaItem newItem = baseMediaItem();
        MediaItem persisted = baseMediaItem();
        persisted.setId(42L);
        MediaLibrary library = baseLibrary(newItem.getLibraryId());

        doAnswer(invocation -> {
            MediaItem argument = invocation.getArgument(0);
            argument.setId(42L);
            return 1;
        }).when(mediaItemMapper).insert(newItem);

        when(mediaItemMapper.selectById(42L)).thenReturn(persisted);
        when(mediaLibraryMapper.selectById(newItem.getLibraryId())).thenReturn(library);
        when(mediaLibraryMapper.selectById(persisted.getLibraryId())).thenReturn(library);
        when(mediaTagRelationService.listTagIds(42L)).thenReturn(List.of(1L, 2L));

        MediaItemDetail detail = mediaItemService.create(new MediaItemCommand(newItem, List.of(1L, 2L)));

        assertThat(detail.mediaItem()).isEqualTo(persisted);
        assertThat(detail.mediaLibrary()).isEqualTo(library);
        assertThat(detail.tagIds()).containsExactly(1L, 2L);
        verify(mediaTagRelationService).replaceTags(42L, List.of(1L, 2L));
    }

    @Test
    void updateWithoutTagPayloadSkipsRelationSync() {
        MediaItem stored = baseMediaItem();
        stored.setId(7L);
        MediaLibrary library = baseLibrary(stored.getLibraryId());
        when(mediaItemMapper.selectById(7L)).thenReturn(stored);
        when(mediaLibraryMapper.selectById(stored.getLibraryId())).thenReturn(library);
        when(mediaTagRelationService.listTagIds(7L)).thenReturn(Collections.emptyList());

        MediaItem patch = new MediaItem();
        patch.setCustomTitle("new-title");

        MediaItemDetail detail = mediaItemService.update(7L, new MediaItemCommand(patch, null));

        assertThat(detail.mediaItem().getCustomTitle()).isEqualTo("new-title");
        verify(mediaTagRelationService, never()).replaceTags(anyLong(), anyList());
    }

    @Test
    void deleteClearsRelationsBeforeRemovingMedia() {
        MediaItem stored = baseMediaItem();
        stored.setId(9L);
        when(mediaItemMapper.selectById(9L)).thenReturn(stored);

        mediaItemService.delete(9L);

        verify(mediaTagRelationService).replaceTags(9L, Collections.emptyList());
        verify(mediaItemMapper).delete(9L);
    }

    private MediaItem baseMediaItem() {
        MediaItem item = new MediaItem();
        item.setUserId(1L);
        item.setLibraryId(100L);
        item.setStatus("planned");
        item.setCustomTitle("title");
        item.setNotes("notes");
        item.setRating(8.0);
        LocalDate today = LocalDate.now();
        item.setStartDate(Date.valueOf(today));
        item.setFinishDate(Date.valueOf(today));
        item.setCustomCoverUrl("http://example.com");
        item.setCustomSource("manual");
        item.setDeletedAt(null);
        item.setRowVersion(1L);
        return item;
    }

    private MediaLibrary baseLibrary(Long id) {
        MediaLibrary library = new MediaLibrary();
        library.setId(id);
        library.setType("anime");
        library.setTitle("library-title");
        library.setOriginalTitle("orig");
        library.setYear(2020);
        library.setCoverUrl("http://example.com/library");
        library.setSource("manual");
        library.setMeta("{}");
        library.setCreatedAt(Timestamp.from(Instant.now()));
        library.setUpdatedAt(Timestamp.from(Instant.now()));
        return library;
    }
}
