package com.android.wannasing.feature.account.register.viewcontroller;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;
import com.android.wannasing.common.model.User;
import com.android.wannasing.common.viewcontroller.FireDb;
import com.android.wannasing.common.viewcontroller.FireDb.TransactionManager;
import com.android.wannasing.common.viewcontroller.ServerDbLifeCycleManager;
import com.android.wannasing.databinding.ActivityRegisterBinding;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

  public static final ActivityResultContract<Intent, String> contract =
      new ActivityResultContract<Intent, String>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Intent input) {
          return input;
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
          if (resultCode != Activity.RESULT_OK || intent == null) {
            return null;
          }
          return intent.getStringExtra("EMAIL");
        }
      };

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
    // ServerDb 설정.
    ServerDbLifeCycleManager serverDbLifeCycleManager = new ServerDbLifeCycleManager();
    getLifecycle().addObserver(serverDbLifeCycleManager);
    userFireDb = new FireDb<>("user_list", User.class);
    serverDbLifeCycleManager.add(userFireDb);
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
      return;
    }
    try {
      inputBirthDate = new GregorianCalendar(
          Integer.parseInt(inputBirthStrList.get(0)),
          Integer.parseInt(inputBirthStrList.get(1)) - 1,
          Integer.parseInt(inputBirthStrList.get(2))).getTime();
    } catch (NumberFormatException e) {
      Toast.makeText(RegisterActivity.this,
          "날짜를 입력해 주세요.",
          Toast.LENGTH_SHORT)
          .show();
      return;
    }

    // 비밀번호 체크.
    if (inputPwd.equals(inputPwdCheck)) {
      Utilities.log(LogType.d, "등록 버튼 눌림" +
          "\nemail : " + inputEmail +
          "\npwd : " + inputPwd);
      // 로딩 띄움.
      ProgressDialog loadDialog = new ProgressDialog(RegisterActivity.this);
      loadDialog.setMessage("가입 중입니다...");
      loadDialog.show();

      // auth 서버에 새로운 계정 등록.
      firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPwd)
          .addOnSuccessListener(authResult -> {
            loadDialog.dismiss();

            FirebaseUser user = firebaseAuth.getCurrentUser();
            assert user != null;
            String userEmail = user.getEmail();
            assert userEmail != null;
            String userId = user.getUid();
            User newUserData = new User(
                inputName,
                userId,
                inputNick,
                userEmail,
                inputPhone,
                inputBirthDate);
            // user DB 에 사용자 정보 저장.
            transactionManager.run(transaction -> {
              userFireDb.setData(newUserData, transaction);
              return null;
            });
            // 회원 가입 화면 빠져나오기.
            Intent intent = new Intent();
            intent.putExtra("EMAIL", inputEmail);
            setResult(Activity.RESULT_OK, intent);
            Toast.makeText(RegisterActivity.this,
                "회원가입에 성공하셨습니다.",
                Toast.LENGTH_SHORT)
                .show();
            finish();

          }).addOnFailureListener(err -> {
        loadDialog.dismiss();
        Utilities.log(LogType.w, "error.\n" +
            err.getMessage());
      });
    } else {
      Toast.makeText(RegisterActivity.this,
          "비밀번호가 틀렸습니다. 다시 입력해 주세요.",
          Toast.LENGTH_SHORT)
          .show();
    }
  }
}