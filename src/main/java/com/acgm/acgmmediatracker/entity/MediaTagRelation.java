package com.acgm.acgmmediatracker.entity;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class MediaTagRelation {

  private long mediaId;
  private long tagId;
  private Timestamp createTime;

}
