package com.acgm.acgmmediatracker.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class User {

  private long id;
  private String username;
  private String email;
  private String emailNorm;
  private String preference;
  private String password;
  private String role;
  private java.sql.Timestamp createdAt;
  private java.sql.Timestamp updatedAt;


}
