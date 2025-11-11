package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgressMusic {

  private long mediaId;
  private long playCount;
  private String listenStatus;
  private java.sql.Timestamp updatedAt;


}
