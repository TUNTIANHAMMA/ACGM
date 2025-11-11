package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgressComic {

  private long mediaId;
  private long currentChapter;
  private Long totalChapters;
  private Long currentVolume;
  private java.sql.Timestamp updatedAt;


}
