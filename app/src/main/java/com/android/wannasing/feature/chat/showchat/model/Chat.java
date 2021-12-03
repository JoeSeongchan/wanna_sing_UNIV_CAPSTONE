package com.android.wannasing.feature.chat.showchat.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.android.wannasing.common.model.old.Data;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

@Entity(tableName = "chat_table")
public class Chat implements Serializable, Data {

  // default constants that fields have.
  private static final String DEF_GROUP_ID = "def_group_id";
  private static final String DEF_MSG = "def_msg";
  private static final String DEF_NICK = "def_nick";
  private static final Timestamp DEF_TIME;

  static {
    Date dumDate = new Date();
    new GregorianCalendar(1900, 0, 1).setTime(dumDate);
    DEF_TIME = new Timestamp(dumDate);
  }

  // columns (=fields)
  @PrimaryKey
  @NonNull
  private String chatId = AUTO_GENERATED_KEY;
  @NonNull
  private String groupId = DEF_GROUP_ID;
  @NonNull
  private String msg = DEF_MSG;
  @NonNull
  private String nickname = DEF_NICK;
  @ServerTimestamp
  @NonNull
  private Timestamp timestamp = DEF_TIME;
  @Exclude
  private boolean isUpdated = false;

  @Ignore
  public Chat(@NonNull String groupId,
      @NonNull String nickname,
      @NonNull String msg) {
    this.groupId = groupId;
    this.msg = msg;
    this.nickname = nickname;
  }

  public Chat() {
  }

  @NonNull
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(@NonNull String groupId) {
    this.groupId = groupId;
  }

  public boolean isUpdated() {
    return isUpdated;
  }

  public void setUpdated(boolean updated) {
    isUpdated = updated;
  }

  @NonNull
  public String getChatId() {
    return this.chatId;
  }

  public void setChatId(@NonNull String chatId) {
    this.chatId = chatId;
  }

  @NonNull
  public String getMsg() {
    return msg;
  }

  public void setMsg(@NonNull String msg) {
    this.msg = msg;
  }

  @NonNull
  public String getNickname() {
    return nickname;
  }

  public void setNickname(@NonNull String nickname) {
    this.nickname = nickname;
  }

  @NonNull
  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@NonNull Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String getPrimaryKey() {
    return this.chatId;
  }

  @Override
  public void setPrimaryKey(String primaryKey) {
    this.chatId = primaryKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Chat)) {
      return false;
    }
    Chat chat = (Chat) o;
    return getChatId().equals(chat.getChatId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getChatId());
  }
}
