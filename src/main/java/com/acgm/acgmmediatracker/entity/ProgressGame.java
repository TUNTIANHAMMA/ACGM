package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgressGame {

  private long mediaId;
  private String platform;
  private long completionPercent;
  private long playTimeHours;
  private java.sql.Timestamp updatedAt;


}
