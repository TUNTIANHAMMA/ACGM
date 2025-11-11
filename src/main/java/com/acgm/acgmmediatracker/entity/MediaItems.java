package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MediaItems {

  private long id;
  private long userId;
  private String type;
  private String status;
  private String title;
  private String notes;
  private double rating;
  private java.sql.Date startDate;
  private java.sql.Date finishDate;
  private String coverUrl;
  private String source;
  private java.sql.Timestamp createdAt;
  private java.sql.Timestamp updatedAt;
  private java.sql.Timestamp deletedAt;
  private long rowVersion;
  private long finishMonth;


}
