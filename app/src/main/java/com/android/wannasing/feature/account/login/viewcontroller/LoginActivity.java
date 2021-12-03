package com.android.wannasing.feature.account.login.viewcontroller;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.common.model.User;
import com.android.wannasing.common.viewcontroller.FireDb;
import com.android.wannasing.common.viewcontroller.ServerDbLifeCycleManager;
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

  private EditText etEmail, etPwd;
  private final ActivityResultLauncher<Intent> lchRegister = registerForActivityResult(
      RegisterActivity.contract, email -> {
        if (email == null) {
          Utilities.log(LogType.d, "sign up fail.");
        } else {
          Utilities.log(LogType.d, "sign up success.");
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
    moveToNextAct();
  }

  private void setUi() {
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    etEmail = binding.loginEtEmail;
    etPwd = binding.loginEtPwd;
    binding.loginTvRegister.setOnClickListener(this::signUp);
    binding.loginBtnLogin.setOnClickListener(this::login);
    inputChecker = new TextWatcherSetterForLogin(binding);
    inputChecker.run();
  }

  private void setDb() {
    authDb = FirebaseAuth.getInstance();

    ServerDbLifeCycleManager serverDbLifeCycleManager = new ServerDbLifeCycleManager();
    getLifecycle().addObserver(serverDbLifeCycleManager);
    userFireDb = new FireDb<>("user_list", User.class);
    serverDbLifeCycleManager.add(userFireDb);
  }

  private void signUp(@NonNull View view) {
    lchRegister.launch(new Intent(this, RegisterActivity.class));
  }

  private void login(@NonNull View view) {
    if (!inputChecker.isInputProper()) {
      binding.loginTvErrorMessage.setText("정보를 끝까지 입력하세요.");
      binding.loginTvErrorMessage.setVisibility(View.VISIBLE);
      return;
    }
    binding.loginTvErrorMessage.setVisibility(View.INVISIBLE);
    String email = etEmail.getText().toString().trim();
    String pwd = etPwd.getText().toString().trim();

    authDb.signInWithEmailAndPassword(email, pwd)
        .addOnSuccessListener(authResult -> {
          Utilities.log(LogType.d, "login success.");
          moveToNextAct();
        })
        .addOnFailureListener(err -> {
              Utilities.log(LogType.d, "login error : " + err.getMessage());
              binding.loginTvErrorMessage.setText("아이디나 비밀번호가 일치하지 않습니다.");
              binding.loginTvErrorMessage.setVisibility(View.VISIBLE);
            }
        );
  }


  private void moveToNextAct(User user) {
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    bundle.putSerializable("USER", user);
    intent.putExtras(bundle);
    startActivity(intent);
    finish();
  }

  // 다음 Activity 로 넘어가는 함수. (로그인 확인)
  private void moveToNextAct() {
    Optional<FirebaseUser> opCurrentUser = Optional.ofNullable(authDb.getCurrentUser());
    if (opCurrentUser.isPresent()) {
      Intent intent = new Intent(this, HomeActivity.class);
      userFireDb.getData(String.valueOf(Objects.hash(opCurrentUser.get().getUid())))
          .subscribe(opUser -> {
            if (opUser.isPresent()) {
              intent.putExtra("USER_INFO", opUser.get());
              startActivity(intent);
              finish();
            } else {
              Utilities.log(LogType.w, "err: user null.");
            }
          });
    } else {
      Utilities.log(LogType.w, "login fail.");
    }
  }
}