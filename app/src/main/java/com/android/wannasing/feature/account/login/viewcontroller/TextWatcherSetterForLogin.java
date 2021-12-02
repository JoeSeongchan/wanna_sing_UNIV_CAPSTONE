package com.android.wannasing.feature.account.login.viewcontroller;


import android.text.TextWatcher;
import com.android.wannasing.common.viewcontroller.TextWatcherFactory;
import com.android.wannasing.databinding.ActivityLoginBinding;
import com.android.wannasing.feature.account.login.viewcontroller.InputConstraintsForLogin.CheckResult;
import com.android.wannasing.feature.account.login.viewcontroller.InputConstraintsForLogin.CheckType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class TextWatcherSetterForLogin {

  private final ActivityLoginBinding binding;
  private final Map<CheckType, CheckResult> checkResultList;


  public TextWatcherSetterForLogin(
      ActivityLoginBinding binding) {
    this.binding = binding;
    checkResultList = new HashMap<>();
  }

  private void setEmailConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      // email check.
      CheckResult checkResult = InputConstraintsForLogin.isEmail(s);
      switch (checkResult) {
        case PATTERN_ERR:
        default:
          binding.loginTilEmail.setError("이메일을 제대로 입력해주세요.");
          break;
        case OK:
          binding.loginTilEmail.setErrorEnabled(false);
          break;
      }
      checkResultList.put(CheckType.EMAIL, checkResult);
    }, (s) -> {
    });
    binding.loginEtEmail.addTextChangedListener(watcher);
  }

  private void setPwdConstraints() {
    TextWatcher watcher = TextWatcherFactory.create((s) -> {
    }, (s) -> {
      CheckResult checkResult;
      if (InputConstraintsForLogin.PWD_MIN_LEN <= s.length()
          && s.length() <= InputConstraintsForLogin.PWD_MAX_LEN) {
        checkResult = CheckResult.OK;
        binding.loginTilPwd.setErrorEnabled(false);
      } else {
        checkResult = CheckResult.LENGTH_ERR;
        binding.loginEtPwd.setError(String
            .format(Locale.KOREAN, "%d 자 ~ %d 자 범위에서 입력해주세요.",
                InputConstraintsForLogin.PWD_MIN_LEN,
                InputConstraintsForLogin.PWD_MAX_LEN));
      }
      checkResultList.put(CheckType.PWD, checkResult);
    }, (s) -> {
    });
    binding.loginEtPwd.addTextChangedListener(watcher);
  }

  public boolean isInputProper() {
    for (Entry<InputConstraintsForLogin.CheckType, InputConstraintsForLogin.CheckResult> checkResult : checkResultList
        .entrySet()) {
      if (checkResult.getValue() != InputConstraintsForLogin.CheckResult.OK) {
        return false;
      }
    }
    return true;
  }

  public void run() {
    setEmailConstraints();
    setPwdConstraints();
  }
}
