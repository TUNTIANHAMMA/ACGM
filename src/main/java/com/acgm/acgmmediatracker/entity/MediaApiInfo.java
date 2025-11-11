package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MediaApiInfo {

  private long id;
  private long mediaId;
  private String apiSource;
  private String apiId;
  private String payload;
  private java.sql.Timestamp lastSync;


}
