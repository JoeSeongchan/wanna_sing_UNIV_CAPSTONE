package com.android.wannasing.home.fragment.myprofile.changing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;

public class ChangingMyProfileActivity extends AppCompatActivity {

  ImageButton local, genre;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_changing_my_profile);
    local = findViewById(R.id.changingMyProfile_btn_changeLocal);
    genre = findViewById(R.id.changingMyProfile_btn_changeGenre);
    click();
  }

  private void click() {
    local.setOnClickListener(view -> {
      Intent intent = new Intent(getApplicationContext(),
          GettingInputForChangingMyProfileActivity.class);
      intent.putExtra("option", "활동지역 변경");
      startActivity(intent);
    });
    genre.setOnClickListener(view -> {
      Intent intent = new Intent(getApplicationContext(),
          GettingInputForChangingMyProfileActivity.class);
      intent.putExtra("option", "선호장르 변경");
      startActivity(intent);
    });
  }


}
