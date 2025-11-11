package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Reviews {

  private long id;
  private long userId;
  private long mediaId;
  private double rating;
  private String content;
  private java.sql.Timestamp createdAt;


}
