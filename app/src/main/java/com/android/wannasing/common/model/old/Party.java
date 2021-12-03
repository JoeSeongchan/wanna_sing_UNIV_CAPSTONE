package com.android.wannasing.common.model.old;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

@Deprecated
@Entity(tableName = "party_table")
public class Party implements Serializable, Data {

  // default constants that fields have.
  public static final Timestamp DEF_TIME;
  private static final String DEF_TITLE = "def_title";
  private static final String DEF_HOST = "def_host";
  private static final int DEF_MAX_NUM = 0;
  private static final String DEF_PIC = "def_pic";
  private static final int DEF_CUR_NUM = 0;
  private static final String DEF_KAR_ID = "def_kar_id";

  static {
    Date dumDate = new Date();
    new GregorianCalendar(1900, 0, 1).setTime(dumDate);
    DEF_TIME = new Timestamp(dumDate);
  }

  // columns (=fields)
  @PrimaryKey
  @NonNull
  private String id = AUTO_GENERATED_KEY;
  @NonNull
  private String title;
  @NonNull
  private String hostId;
  private int maxUserNum;
  @NonNull
  private String picUri;
  private int curUserNum = DEF_CUR_NUM;
  @ServerTimestamp
  @NonNull
  private Timestamp startDateTime = DEF_TIME;
  @NonNull
  private String karaokeId;
  @Exclude
  private boolean isUpdated = false;

  public Party() {
    title = DEF_TITLE;
    hostId = DEF_HOST;
    maxUserNum = DEF_MAX_NUM;
    picUri = DEF_PIC;
    karaokeId = DEF_KAR_ID;
  }

  @Ignore
  public Party(
      @NonNull String title,
      @NonNull String hostId,
      int maxUserNum,
      @NonNull String picUri,
      @NonNull String karaokeId
  ) {
    this.title = title;
    this.hostId = hostId;
    this.maxUserNum = maxUserNum;
    this.picUri = picUri;
    this.karaokeId = karaokeId;
  }

  public boolean isUpdated() {
    return isUpdated;
  }

  public void setUpdated(boolean updated) {
    isUpdated = updated;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  @NonNull
  public String getTitle() {
    return title;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  @NonNull
  public String getPicUri() {
    return picUri;
  }

  public void setPicUri(@NonNull String picUri) {
    this.picUri = picUri;
  }

  @NonNull
  public String getHostId() {
    return hostId;
  }

  public void setHostId(@NonNull String hostId) {
    this.hostId = hostId;
  }

  public int getMaxUserNum() {
    return maxUserNum;
  }

  public void setMaxUserNum(int maxUserNum) {
    this.maxUserNum = maxUserNum;
  }

  public int getCurUserNum() {
    return curUserNum;
  }

  public void setCurUserNum(int curUserNum) {
    this.curUserNum = curUserNum;
  }

  @NonNull
  public Timestamp getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(@NonNull Timestamp startDateTime) {
    this.startDateTime = startDateTime;
  }

  @NonNull
  public String getKaraokeId() {
    return karaokeId;
  }

  public void setKaraokeId(@NonNull String karaokeId) {
    this.karaokeId = karaokeId;
  }

  @Override
  public String getPrimaryKey() {
    return getId();
  }

  @Override
  public void setPrimaryKey(String key) {
    this.id = key;
  }
}
