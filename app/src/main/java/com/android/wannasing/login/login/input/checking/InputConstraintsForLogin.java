package com.android.wannasing.login.login.input.checking;

import com.android.wannasing.login.register.input.checking.InputConstraintsForRegister;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputConstraintsForLogin {

  public static final int PWD_MIN_LEN = InputConstraintsForRegister.PWD_MIN_LEN;
  public static final int PWD_MAX_LEN = InputConstraintsForRegister.PWD_MAX_LEN;

  public static boolean isPatternMatched(String regex, String input) {
    boolean returnValue = false;
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    if (m.matches()) {
      returnValue = true;
    }
    return returnValue;
  }

  public static CheckResult isEmail(String input) {
    if (!isPatternMatched("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$", input)) {
      return CheckResult.PATTERN_ERR;
    } else {
      return CheckResult.OK;
    }
  }

  public enum CheckType {
    EMAIL,
    PWD
  }

  public enum CheckResult {
    OK,
    PATTERN_ERR,
    LENGTH_ERR
  }

}
