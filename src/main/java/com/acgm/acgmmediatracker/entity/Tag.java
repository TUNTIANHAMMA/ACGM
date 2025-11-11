package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tag {

  private long id;
  private long userId;
  private String name;
  private java.sql.Timestamp createdAt;


}
