package com.acgm.acgmmediatracker.mapper;

import com.acgm.acgmmediatracker.entity.MediaItem;
import com.acgm.acgmmediatracker.entity.MediaTagRelation;
import com.acgm.acgmmediatracker.entity.Tag;
import com.acgm.acgmmediatracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MediaTagRelationMapperTest {

    @Autowired
    private MediaTagRelationMapper relationMapper;

    @Autowired
    private MapperTestHelper helper;

    @Test
    void insertAndQueryRelations() {
        User user = helper.createUser();
        MediaItem mediaItem = helper.createMediaItem(user.getId(), "anime");
        Tag tag = helper.createTag(user.getId());

        MediaTagRelation relation = new MediaTagRelation();
        relation.setMediaId(mediaItem.getId());
        relation.setTagId(tag.getId());
        relationMapper.insert(relation);

        MediaTagRelation fetched = relationMapper.selectOne(mediaItem.getId(), tag.getId());
        assertThat(fetched).isNotNull();

        List<MediaTagRelation> relations = relationMapper.selectByMediaId(mediaItem.getId());
        assertThat(relations)
                .extracting(MediaTagRelation::getTagId)
                .contains(tag.getId());
    }
}
