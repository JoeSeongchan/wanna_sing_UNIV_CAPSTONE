package com.android.wannasing.feature.account.register.viewcontroller;

import android.text.TextWatcher;
import com.android.wannasing.common.viewcontroller.TextWatcherFactory;
import com.android.wannasing.databinding.ActivityRegisterBinding;
import com.android.wannasing.feature.account.register.viewcontroller.InputConstraintsForRegister.CheckResult;
import com.android.wannasing.feature.account.register.viewcontroller.InputConstraintsForRegister.CheckType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class TextWatcherSetterForRegister {

  private final ActivityRegisterBinding binding;
  private final Map<CheckType, CheckResult> checkResultList;


  public TextWatcherSetterForRegister(
      ActivityRegisterBinding binding) {
    this.binding = binding;
    checkResultList = new HashMap<>();
  }

  private void setNameConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      // name check.
      CheckResult checkResult = InputConstraintsForRegister.isName(s);
      switch (checkResult) {
        case LENGTH_ERR:
          binding.registerTilName.setError(String
              .format(Locale.KOREAN, "%d 자 ~ %d 자 범위에서 입력해주세요.",
                  InputConstraintsForRegister.NAME_MIN_LEN,
                  InputConstraintsForRegister.NAME_MAX_LEN));
          break;
        case PATTERN_ERR:
        default:
          binding.registerTilName.setError("이름을 제대로 입력해주세요. (한글, 영어, 공백만 허용)");
          break;
        case OK:
          binding.registerTilName.setErrorEnabled(false);
          break;
      }
      checkResultList.put(CheckType.NAME, checkResult);
    }, (s) -> {
    });
    binding.registerEtName.addTextChangedListener(watcher);
  }

  private void setNickConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      // nickname check.
      CheckResult checkResult = InputConstraintsForRegister.isNickname(s);
      switch (checkResult) {
        case LENGTH_ERR:
          binding.registerTilNick.setError(String
              .format(Locale.KOREAN, "%d 자 ~ %d 자 범위에서 입력해주세요.",
                  InputConstraintsForRegister.NICK_MIN_LEN,
                  InputConstraintsForRegister.NICK_MAX_LEN));
          break;
        case PATTERN_ERR:
        default:
          binding.registerTilNick.setError("닉네임을 제대로 입력해주세요. (한글, 영어, 숫자, 공백만 허용)");
          break;
        case OK:
          binding.registerTilNick.setErrorEnabled(false);
          break;
      }
      checkResultList.put(CheckType.NICK, checkResult);
    }, (s) -> {
    });
    binding.registerEtNick.addTextChangedListener(watcher);
  }

  private void setEmailConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      // email check.
      CheckResult checkResult = InputConstraintsForRegister.isEmail(s);
      switch (checkResult) {
        case PATTERN_ERR:
        default:
          binding.registerTilEmail.setError("이메일을 제대로 입력해주세요.");
          break;
        case OK:
          binding.registerTilEmail.setErrorEnabled(false);
          break;
      }
      checkResultList.put(CheckType.EMAIL, checkResult);
    }, (s) -> {
    });
    binding.registerEtEmail.addTextChangedListener(watcher);
  }

  private void setPhoneNumConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      // phone number check
      CheckResult checkResult = InputConstraintsForRegister.isPhoneNumber(s);
      switch (checkResult) {
        case PATTERN_ERR:
        default:
          binding.registerTilPhone.setError("전화번호를 제대로 입력해주세요.");
          break;
        case OK:
          binding.registerTilPhone.setErrorEnabled(false);
          break;
      }
      checkResultList.put(CheckType.PHONE_NUMBER, checkResult);
    }, (s) -> {
    });
    binding.registerEtPhone.addTextChangedListener(watcher);
  }

  private void setPwdConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      CheckResult checkResult;
      if (InputConstraintsForRegister.PWD_MIN_LEN <= s.length()
          && s.length() <= InputConstraintsForRegister.PWD_MAX_LEN) {
        checkResult = CheckResult.OK;
        binding.registerTilPwd.setErrorEnabled(false);
      } else {
        checkResult = CheckResult.LENGTH_ERR;
        binding.registerTilPwd.setError(String
            .format(Locale.KOREAN, "%d 자 ~ %d 자 범위에서 입력해주세요.",
                InputConstraintsForRegister.PWD_MIN_LEN,
                InputConstraintsForRegister.PWD_MAX_LEN));
      }
      checkResultList.put(CheckType.PWD, checkResult);
    }, (s) -> {
    });
    binding.registerEtPwd.addTextChangedListener(watcher);
  }

  private void setPwdCheckConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      CheckResult checkResult;
      if (s.compareTo(binding.registerEtPwd.getText().toString()) != 0) {
        checkResult = CheckResult.PATTERN_ERR;
        binding.registerTilPwdCheck.setError("비밀번호가 일치하지 않습니다.");
      } else {
        checkResult = CheckResult.OK;
        binding.registerTilPwdCheck.setErrorEnabled(false);
      }
      checkResultList.put(CheckType.PWD_CHECK, checkResult);
    }, (s) -> {
    });
    binding.registerEtPwdCheck.addTextChangedListener(watcher);
  }

  public boolean isInputProper() {
    for (Entry<CheckType, CheckResult> checkResult : checkResultList.entrySet()) {
      if (checkResult.getValue() != CheckResult.OK) {
        return false;
      }
    }
    return true;
  }

  public void run() {
    setNameConstraints();
    setNickConstraints();
    setEmailConstraints();
    setPhoneNumConstraints();
    setPwdConstraints();
    setPwdCheckConstraints();
  }
}
