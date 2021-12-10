package com.android.wannasing.feature.chat.showchatgroup.model;

import com.android.wannasing.common.model.Entity;
import com.android.wannasing.feature.party.common.model.Party;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ChatGroup implements Entity, Serializable {

  private Party party;

  public ChatGroup(Party party) {
    this.party = party;
  }

  public String getName() {
    return party.partyName;
  }

  public Date getMeetingDate() {
    return party.meetingDate;
  }

  @Override
  public String getPrimaryKey() {
    return party.getPrimaryKey();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChatGroup)) {
      return false;
    }
    ChatGroup chatGroup = (ChatGroup) o;
    return Objects.equals(party, chatGroup.party);
  }

  @Override
  public int hashCode() {
    return party.hashCode();
  }

}
