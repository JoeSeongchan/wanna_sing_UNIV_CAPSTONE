package com.android.wannasing.feature.account.login.viewcontroller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.common.model.User;
import com.android.wannasing.common.viewcontroller.FireDb;
import com.android.wannasing.common.viewcontroller.FireDbLifeCycleObserver;
import com.android.wannasing.databinding.ActivityLoginBinding;
import com.android.wannasing.feature.account.register.viewcontroller.RegisterActivity;
import com.android.wannasing.feature.home.viewcontroller.HomeActivity;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;
import java.util.Optional;

public class LoginActivity extends AppCompatActivity {

  public static final String FROM_REGISTER_ACT_DATA_TAG = "FROM_REGISTER_ACT_DATA_TAG";
  // register activity 에게 데이터를 어떻게 받을 것인지 지정.
  public static final ActivityResultContract<Intent, String> contractForRegisterAct =
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
          return intent.getStringExtra(FROM_REGISTER_ACT_DATA_TAG);
        }
      };
  private EditText etEmail, etPwd;
  private final ActivityResultLauncher<Intent> launcherForRegisterAct = registerForActivityResult(
      contractForRegisterAct, email -> {
        if (email == null) {
          Utilities.log(LogType.d, "SIGN UP FAIL.");
        } else {
          Utilities.log(LogType.d, "SIGN UP SUCCESS.");
          etEmail.setText(email);
        }
      });

  private FirebaseAuth authDb;
  private FireDb<User> userFireDb;
  private ActivityLoginBinding binding;
  private TextWatcherSetterForLogin inputChecker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUi();
    setDb();
    moveToHomeActivity();
  }

  private void setUi() {
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    etEmail = binding.loginEtEmail;
    etPwd = binding.loginEtPwd;
    inputChecker = new TextWatcherSetterForLogin(binding);
    inputChecker.run();
    binding.loginTvRegister.setOnClickListener(this::signUp);
    binding.loginBtnLogin.setOnClickListener(this::login);
  }

  private void setDb() {
    authDb = FirebaseAuth.getInstance();
    FireDbLifeCycleObserver fireDbLifeCycleObserver = new FireDbLifeCycleObserver();
    getLifecycle().addObserver(fireDbLifeCycleObserver);
    userFireDb = new FireDb<>("user_list", User.class);
    fireDbLifeCycleObserver.add(userFireDb);
  }

  private void signUp(@NonNull View view) {
    launcherForRegisterAct.launch(new Intent(this, RegisterActivity.class));
  }

  private void login(@NonNull View view) {
    if (!inputChecker.isInputProper()) {
      binding.loginTvErrorMessage.setText("정보를 끝까지 입력하세요.");
      binding.loginTvErrorMessage.setVisibility(View.VISIBLE);
    } else {
      binding.loginTvErrorMessage.setVisibility(View.INVISIBLE);
      String email = etEmail.getText().toString().trim();
      String pwd = etPwd.getText().toString().trim();

      authDb.signInWithEmailAndPassword(email, pwd)
          .addOnSuccessListener(authResult -> {
            Utilities.log(LogType.d, "LOGIN SUCCESS.");
            moveToHomeActivity();
          })
          .addOnFailureListener(err -> {
                Utilities.log(LogType.w, "LOGIN FAIL : " + err.getMessage());
                binding.loginTvErrorMessage.setText("아이디나 비밀번호가 일치하지 않습니다.");
                binding.loginTvErrorMessage.setVisibility(View.VISIBLE);
              }
          );
    }
  }

  private void moveToHomeActivity() {
    Optional<FirebaseUser> opCurrentUser = Optional.ofNullable(authDb.getCurrentUser());
    if (opCurrentUser.isPresent()) {
      Intent intent = new Intent(this, HomeActivity.class);
      userFireDb.getData(String.valueOf(Objects.hash(opCurrentUser.get().getUid())))
          .subscribe(opUser -> {
            if (opUser.isPresent()) {
              intent.putExtra(HomeActivity.FROM_LOGIN_ACTIVITY_USER_INFO_DATA_TAG, opUser.get());
              startActivity(intent);
              finish();
            } else {
              Utilities.log(LogType.w, "FAIL : user is null in firestore DB.");
            }
          });
    } else {
      Utilities.log(LogType.w, "FAIL : user is null in auth DB.");
    }
  }
}