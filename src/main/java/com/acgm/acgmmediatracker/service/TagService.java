package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.entity.Tag;

import java.util.List;

public interface TagService {

    Tag getById(long id);

    List<Tag> listAll();

    Tag create(Tag tag);

    Tag update(long id, Tag tag);

    void delete(long id);
}
