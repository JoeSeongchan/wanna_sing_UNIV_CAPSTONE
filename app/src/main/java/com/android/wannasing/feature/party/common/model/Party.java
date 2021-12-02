package com.android.wannasing.feature.party.common.model;

import com.android.wannasing.common.model.Entity;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Party implements Serializable, Entity {

  public String hostID = "";
  public String partyName = "";
  public List<Genre> genreList = null;
  public Gender gender = null;
  public int age = 0;
  // 초반 중반 후반
  public List<AgeDetail> ageDetailList = null;
  public String karaokeId = null;
  public String karaokeName = null;
  public Date meetingDate = null;
  public int curMemberNum = 0;
  public int maxMemberNum = 0;
  public MyTime meetingStartTime = null;
  public MyTime meetingEndTime = null;


  public Party(String hostID, String partyName,
      List<Genre> genreList, Gender gender, int age,
      List<AgeDetail> ageDetailList, String karaokeId, String karaokeName, Date meetingDate,
      int curMemberNum, int maxMemberNum, MyTime meetingStartTime,
      MyTime meetingEndTime) {
    this.hostID = hostID;
    this.partyName = partyName;
    this.genreList = genreList;
    this.gender = gender;
    this.age = age;
    this.ageDetailList = ageDetailList;
    this.karaokeId = karaokeId;
    this.karaokeName = karaokeName;
    this.meetingDate = meetingDate;
    this.curMemberNum = curMemberNum;
    this.maxMemberNum = maxMemberNum;
    this.meetingStartTime = meetingStartTime;
    this.meetingEndTime = meetingEndTime;
  }

  public Party() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Party)) {
      return false;
    }
    Party party = (Party) o;
    return hostID.equals(party.hostID) && partyName.equals(party.partyName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hostID, partyName);
  }

  @Override
  public String getPrimaryKey() {
    return String.valueOf(hashCode());
  }

  public enum AgeDetail {
    EARLY("초반"), MID("중반"), LATE("후반"), FREE("자유");
    private final String value;

    AgeDetail(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }

  public enum Genre {

    FREE("자유"),
    TOP("인기"),
    BALAD("발라드"),
    NORMAL_SONG("가요"),
    POP("팝"),
    HIPHOP("힙합"),
    JPOP("JPOP"),
    OTHER("기타"),
    OLD_SONG("트로트");
    private final String value;

    Genre(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }

  public enum Gender {
    FEMALE("여성"), MALE("남성"), FREE("자유");
    private final String value;

    Gender(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }

  public static class MyTime implements Serializable {

    public int hour;
    public int minutes;


    public MyTime(int hour, int minutes) {
      this.hour = hour;
      this.minutes = minutes;
    }

    public MyTime() {
    }

    public MyTime(LocalTime localTime) {
      this.hour = localTime.getHour();
      this.minutes = localTime.getMinute();
    }

    public long subtract(MyTime target) {
      return (hour - target.hour) * 60L + (minutes - target.minutes);
    }

    public long subtract(LocalTime localTime) {
      return this.subtract(new MyTime(localTime));
    }
  }
}
