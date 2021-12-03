package com.android.wannasing.common.viewcontroller.old;

@Deprecated
public class DbChanges<T> {

  private T data;
  private DbChangesType type;

  public DbChanges(T data, DbChangesType type) {
    this.data = data;
    this.type = type;
  }

  public T getData() {
    return data;
  }

  public DbChangesType getType() {
    return type;
  }

  public enum DbChangesType {ADD, MODIFY, DELETE}
}
