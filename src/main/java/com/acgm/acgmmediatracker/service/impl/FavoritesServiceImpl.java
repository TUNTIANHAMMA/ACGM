package com.acgm.acgmmediatracker.service.impl;

import com.acgm.acgmmediatracker.entity.Favorites;
import com.acgm.acgmmediatracker.mapper.FavoritesMapper;
import com.acgm.acgmmediatracker.service.FavoritesService;
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
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesMapper favoritesMapper;

    @Override
    public Favorites getById(long id) {
        Favorites favorite = favoritesMapper.selectById(id);
        if (favorite == null) {
            throw new ResourceNotFoundException("未找到收藏记录，ID=" + id);
        }
        return favorite;
    }

    @Override
    public List<Favorites> listAll() {
        List<Favorites> favorites = favoritesMapper.selectAll();
        return favorites == null ? Collections.emptyList() : favorites;
    }

    @Override
    @Transactional
    public Favorites create(Favorites favorite) {
        Objects.requireNonNull(favorite, "收藏实体不能为空");
        favoritesMapper.insert(favorite);
        return getById(favorite.getId());
    }

    @Override
    @Transactional
    public Favorites update(long id, Favorites favorite) {
        Objects.requireNonNull(favorite, "收藏实体不能为空");
        Favorites existing = getById(id);
        ServiceBeanUtils.copyNonNullProperties(favorite, existing);
        existing.setId(id);
        favoritesMapper.update(existing);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        getById(id);
        favoritesMapper.delete(id);
    }
}
