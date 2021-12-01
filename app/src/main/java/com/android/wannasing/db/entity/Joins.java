package com.example.detailgrouptest.db.entity;

import androidx.annotation.NonNull;

public class Joins {

  public String memberId;
  public String hostId;
  public String partyName;

  public Joins(@NonNull String memberId, @NonNull String hostId, @NonNull String partyName) {
    this.memberId = memberId;
    this.hostId = hostId;
    this.partyName = partyName;
  }

  public Joins() {
  }
}
