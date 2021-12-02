package com.android.wannasing.login.register.input.checking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputConstraintsForRegister {

  public static final int NAME_MIN_LEN = 2;
  public static final int NAME_MAX_LEN = 10;
  public static final int NICK_MIN_LEN = 5;
  public static final int NICK_MAX_LEN = 10;
  public static final int PWD_MIN_LEN = 6;
  public static final int PWD_MAX_LEN = 15;

  public static boolean isPatternMatched(String regex, String input) {
    boolean returnValue = false;
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    if (m.matches()) {
      returnValue = true;
    }
    return returnValue;
  }


  public static CheckResult isName(String input) {
    if (input.length() < NAME_MIN_LEN || input.length() > NAME_MAX_LEN) {
      return CheckResult.LENGTH_ERR;
    } else if (!isPatternMatched("^[가-힣a-zA-Z][가-힣a-zA-Z ]*$", input)) {
      return CheckResult.PATTERN_ERR;
    } else {
      return CheckResult.OK;
    }
  }

  public static CheckResult isNickname(String input) {
    if (input.length() < NICK_MIN_LEN || input.length() > 10) {
      return CheckResult.LENGTH_ERR;
    } else if (!isPatternMatched("^[가-힣a-zA-Z][가-힣a-zA-Z0-9]+$", input)) {
      return CheckResult.PATTERN_ERR;
    } else {
      return CheckResult.OK;
    }
  }

  public static CheckResult isEmail(String input) {
    if (!isPatternMatched("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$", input)) {
      return CheckResult.PATTERN_ERR;
    } else {
      return CheckResult.OK;
    }
  }

  public static CheckResult isPhoneNumber(String input) {
    if (!isPatternMatched("^01([016789])-?([0-9]{3,4})-?([0-9]{4})$", input)) {
      return CheckResult.PATTERN_ERR;
    } else {
      return CheckResult.OK;
    }
  }

  public enum CheckType {
    NAME,
    NICK,
    EMAIL,
    PHONE_NUMBER,
    PWD,
    PWD_CHECK
  }

  public enum CheckResult {
    OK,
    PATTERN_ERR,
    LENGTH_ERR
  }
}
