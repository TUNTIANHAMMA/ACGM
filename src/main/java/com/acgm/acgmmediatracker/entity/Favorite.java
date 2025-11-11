package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Favorite {

  private long id;
  private long userId;
  private long mediaId;
  private java.sql.Timestamp createdAt;


}
