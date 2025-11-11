package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.Tag;
import com.acgm.acgmmediatracker.mapper.TagMapper;
import com.acgm.acgmmediatracker.service.TagService;
import com.acgm.acgmmediatracker.service.exception.ResourceNotFoundException;
import com.acgm.acgmmediatracker.service.util.ServiceBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public Tag getById(long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new ResourceNotFoundException("未找到标签，ID=" + id);
        }
        return tag;
    }

    @Override
    public List<Tag> listAll() {
        List<Tag> tags = tagMapper.selectAll();
        return tags == null ? Collections.emptyList() : tags;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        Objects.requireNonNull(tag, "标签实体不能为空");
        tagMapper.insert(tag);
        return getById(tag.getId());
    }

    @Override
    @Transactional
    public Tag update(long id, Tag tag) {
        Objects.requireNonNull(tag, "标签实体不能为空");
        Tag existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(tag, existing);
        existing.setId(id);
        tagMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        tagMapper.delete(id);
    }
}
