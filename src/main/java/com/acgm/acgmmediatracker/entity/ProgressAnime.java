package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgressAnime {

  private long mediaId;
  private long currentEpisode;
  private long totalEpisodes;
  private java.sql.Timestamp updatedAt;


}
