package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.Favorite;
import com.acgm.acgmmediatracker.mapper.FavoriteMapper;
import com.acgm.acgmmediatracker.service.FavoriteService;
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
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Override
    public Favorite getById(long id) {
        Favorite favorite = favoriteMapper.selectById(id);
        if (favorite == null) {
            throw new ResourceNotFoundException("未找到收藏记录，ID=" + id);
        }
        return favorite;
    }

    @Override
    public List<Favorite> listAll() {
        List<Favorite> favorites = favoriteMapper.selectAll();
        return favorites == null ? Collections.emptyList() : favorites;
    }

    @Override
    @Transactional
    public Favorite create(Favorite favorite) {
        Objects.requireNonNull(favorite, "收藏实体不能为空");
        favoriteMapper.insert(favorite);
        return getById(favorite.getId());
    }

    @Override
    @Transactional
    public Favorite update(long id, Favorite favorite) {
        Objects.requireNonNull(favorite, "收藏实体不能为空");
        Favorite existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(favorite, existing);
        existing.setId(id);
        favoriteMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        favoriteMapper.delete(id);
    }
}
