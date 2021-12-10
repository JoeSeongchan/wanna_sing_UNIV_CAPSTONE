package com.android.wannasing.feature.chat.showchat.viewcontroller;

import java.util.Date;

// type convertor for room database.
public class TypeConverter {

  @androidx.room.TypeConverter
  public static Date longToDate(Long time) {
    return new Date(time);
  }

  @androidx.room.TypeConverter
  public static Long dateToLong(Date data) {
    return data.getTime();
  }
}