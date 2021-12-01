package com.android.wannasing.db.entity;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Objects;

public class Joins implements Serializable, Entity {

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

  @Override
  public String getPrimaryKey() {
    return String.valueOf(hashCode());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Joins)) {
      return false;
    }
    Joins joins = (Joins) o;
    return memberId.equals(joins.memberId) && hostId.equals(joins.hostId) && partyName
        .equals(joins.partyName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, hostId, partyName);
  }
}
