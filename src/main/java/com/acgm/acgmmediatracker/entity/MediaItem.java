package com.acgm.acgmmediatracker.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaItem {

    private Long id;
    private Long userId;
    private Long libraryId;
    private String status;
    private Double rating;
    private String notes;
    private java.sql.Date startDate;
    private java.sql.Date finishDate;
    private String customTitle;
    private String customCoverUrl;
    private String customSource;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private java.sql.Timestamp deletedAt;
    private Long rowVersion;
    private Integer finishMonth;
}
