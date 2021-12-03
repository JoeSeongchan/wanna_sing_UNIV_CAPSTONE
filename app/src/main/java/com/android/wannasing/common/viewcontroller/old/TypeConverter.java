package com.android.wannasing.common.viewcontroller.old;

import com.google.firebase.Timestamp;
import java.util.Date;

@Deprecated
// type convertor for room database.
public class TypeConverter {

  @androidx.room.TypeConverter
  public static Timestamp longToTimestamp(Long time) {
    return new Timestamp(new Date(time));
  }

  @androidx.room.TypeConverter
  public static Long timestampToLong(Timestamp data) {
    return data.toDate().getTime();
  }
}