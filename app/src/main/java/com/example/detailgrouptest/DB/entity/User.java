package com.example.detailgrouptest.db.entity;

import androidx.annotation.NonNull;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class User {

  @NonNull
  private String name = "def_name";
  @NonNull
  private String id = "def_id";
  @NonNull
  private String nick = "def_nick";
  @NonNull
  private String email = "def_email";
  @NonNull
  private String tel = "def_tel";
  @NonNull
  private Date dateOfBirth = new GregorianCalendar(1900, 1, 1).getTime();

  public User() {
  }

  public User(@NonNull String name, @NonNull String id, @NonNull String nick,
      @NonNull String email, @NonNull String tel, @NonNull Date dateOfBirth) {
    this.name = name;
    this.id = id;
    this.nick = nick;
    this.email = email;
    this.tel = tel;
    this.dateOfBirth = dateOfBirth;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  @NonNull
  public String getNick() {
    return nick;
  }

  public void setNick(@NonNull String nick) {
    this.nick = nick;
  }

  @NonNull
  public String getEmail() {
    return email;
  }

  public void setEmail(@NonNull String email) {
    this.email = email;
  }

  @NonNull
  public String getTel() {
    return tel;
  }

  public void setTel(@NonNull String tel) {
    this.tel = tel;
  }

  @NonNull
  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(@NonNull Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return getId().equals(user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
