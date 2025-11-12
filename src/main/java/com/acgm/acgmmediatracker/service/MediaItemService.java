package com.acgm.acgmmediatracker.service;

import com.acgm.acgmmediatracker.dto.media.MediaItemCommand;
import com.acgm.acgmmediatracker.dto.media.MediaItemDetail;

import java.util.List;

public interface MediaItemService {

    MediaItemDetail getDetail(long id);

    List<MediaItemDetail> listAll();

    MediaItemDetail create(MediaItemCommand command);

    MediaItemDetail update(long id, MediaItemCommand command);

    void delete(long id);
}
