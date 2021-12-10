package com.android.wannasing.feature.account.register.viewcontroller;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;
import com.android.wannasing.common.model.User;
import com.android.wannasing.common.viewcontroller.FireDb;
import com.android.wannasing.common.viewcontroller.FireDb.TransactionManager;
import com.android.wannasing.common.viewcontroller.FireDbLifeCycleObserver;
import com.android.wannasing.databinding.ActivityRegisterBinding;
import com.android.wannasing.feature.account.login.viewcontroller.LoginActivity;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

  public static final String USER_COLLECTION_PATH = "user_list";
  // 컴포넌트.
  private EditText etNick, etName, etEmail, etPhone, etPwd, etPwdCheck;
  private TextView tvBirthInput;
  private Button btnRegister;
  private ActivityRegisterBinding binding;
  // DB.
  private FirebaseAuth firebaseAuth;
  private FireDb<User> userFireDb;
  private TransactionManager transactionManager;
  private TextWatcherSetterForRegister inputChecker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    binding = ActivityRegisterBinding.inflate(getLayoutInflater());
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());
    setUi();
    setDb();
  }

  private void setUi() {
    etNick = binding.registerEtNick;
    etName = binding.registerEtName;
    etEmail = binding.registerEtEmail;
    etPhone = binding.registerEtPhone;
    tvBirthInput = binding.registerTvBirthInput;
    etPwd = binding.registerEtPwd;
    etPwdCheck = binding.registerEtPwdCheck;
    btnRegister = binding.btnRegisterRegister;
    btnRegister.setOnClickListener(this::registerAccount);
    tvBirthInput.setOnClickListener(this::setBirthDate);

    inputChecker = new TextWatcherSetterForRegister(binding);
    inputChecker.run();
  }


  // DB 설정하는 함수. (인증서버)
  private void setDb() {
    firebaseAuth = FirebaseAuth.getInstance();
    transactionManager = new TransactionManager();
    FireDbLifeCycleObserver fireDbLifeCycleObserver = new FireDbLifeCycleObserver();
    getLifecycle().addObserver(fireDbLifeCycleObserver);
    userFireDb = new FireDb<>(USER_COLLECTION_PATH, User.class);
    fireDbLifeCycleObserver.add(userFireDb);
  }

  private void setBirthDate(View view) {
    OnDateSetListener onDateSetListener = (datePicker, year, month, day) ->
        tvBirthInput.setText(getString(R.string.tv_birth_date_form, year, month, day));
    DatePickerDialog dialog = new DatePickerDialog(
        this,
        onDateSetListener,
        2021,
        11,
        15);
    dialog.show();
  }

  // 등록 버튼 클릭 리스너.
  private void registerAccount(@NonNull View view) {
    // 사용자가 입력한 정보 가져오기.
    String inputNick = etNick.getText().toString().trim();
    String inputName = etName.getText().toString().trim();
    String inputEmail = etEmail.getText().toString().trim();
    String inputPhone = etNick.getText().toString().trim();
    String inputPwd = etPwd.getText().toString().trim();
    String inputPwdCheck = etPwdCheck.getText().toString().trim();
    List<String> inputBirthStrList = Arrays
        .asList(tvBirthInput.getText().toString().trim().split("/"));
    Date inputBirthDate;

    if (!inputChecker.isInputProper()) {
      showWarning("정보를 정확히 입력해주세요.");
      return;
    }
    try {
      inputBirthDate = new GregorianCalendar(
          Integer.parseInt(inputBirthStrList.get(0)),
          Integer.parseInt(inputBirthStrList.get(1)),
          Integer.parseInt(inputBirthStrList.get(2))).getTime();
    } catch (NumberFormatException e) {
      showWarning("날짜를 입력해주세요.");
      return;
    }
    if (inputPwd.compareTo(inputPwdCheck) != 0) {
      showWarning("비밀번호가 틀렸습니다. 다시 입력해주세요.");
      return;
    }
    // auth 서버에 새로운 계정 등록.
    firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPwd)
        .addOnSuccessListener(authResult -> {
          FirebaseUser user = firebaseAuth.getCurrentUser();
          if (user == null) {
            Utilities.log(LogType.w, "FAIL : user is null in auth DB.");
          } else {
            String userEmail = user.getEmail();
            String userId = user.getUid();
            User newUserData = new User(
                inputName,
                userId,
                inputNick,
                userEmail,
                inputPhone,
                inputBirthDate);
            transactionManager.run(transaction -> {
              userFireDb.setData(newUserData, transaction);
              return null;
            });
            Intent intent = new Intent();
            intent.putExtra(LoginActivity.FROM_REGISTER_ACT_DATA_TAG, inputEmail);
            setResult(Activity.RESULT_OK, intent);
            showMessage("회원가입 성공.");
            finish();
          }
        }).addOnFailureListener(err -> {
      Utilities.log(LogType.w, "FAIL : can't add user into auth DB.");
    });
  }

  private void showMessage(String msg) {
    Toast.makeText(RegisterActivity.this,
        msg,
        Toast.LENGTH_SHORT)
        .show();
  }

  private void showWarning(String msg) {
    Toast.makeText(RegisterActivity.this,
        msg,
        Toast.LENGTH_SHORT)
        .show();
  }
}