package com.android.wannasing.feature.profile.editprofile.viewcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;

public class EditProfileActivity extends AppCompatActivity {

  ImageButton local, genre;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_profile);
    local = findViewById(R.id.editProfile_btn_changeLocal);
    genre = findViewById(R.id.editProfile_btn_changeGenre);
    click();
  }

  private void click() {
    local.setOnClickListener(view -> {
      Intent intent = new Intent(getApplicationContext(),
          GetNewProfileActivity.class);
      intent.putExtra("option", "활동지역 변경");
      startActivity(intent);
    });
    genre.setOnClickListener(view -> {
      Intent intent = new Intent(getApplicationContext(),
          GetNewProfileActivity.class);
      intent.putExtra("option", "선호장르 변경");
      startActivity(intent);
    });
  }


}
