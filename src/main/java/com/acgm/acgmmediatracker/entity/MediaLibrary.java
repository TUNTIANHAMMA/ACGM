package com.acgm.acgmmediatracker.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaLibrary {

    private Long id;
    private String type;
    private String title;
    private String originalTitle;
    private Integer year;
    private String coverUrl;
    private String source;
    private String meta;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
}
