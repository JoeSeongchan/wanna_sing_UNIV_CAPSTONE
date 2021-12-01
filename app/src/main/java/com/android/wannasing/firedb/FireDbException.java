package com.android.wannasing.firedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.wannasing.utilities.Utilities;
import com.android.wannasing.utilities.Utilities.LogType;

public class FireDbException extends Exception {

  @NonNull
  private final Code code;

  public FireDbException(@NonNull String message,
      @NonNull Code code) {
    super(message);
    this.code = code;
  }

  @Nullable
  @Override
  public String getMessage() {
    return String.format("code : %s | msg : %s", code.name(), super.getMessage());
  }

  public enum Code {
    OK(0),

    COLLISION(1),

    UNKNOWN(2),

    NOT_FOUND(3);

    private final int value;

    Code(int value) {
      this.value = value;
    }

    public static Code getCode(int value) {
      try {
        return Code.values()[value];
      } catch (ArrayIndexOutOfBoundsException e) {
        Utilities.log(LogType.w, "error : " + e.getMessage());
        return UNKNOWN;
      }
    }

    public int value() {
      return value;
    }
  }
}
