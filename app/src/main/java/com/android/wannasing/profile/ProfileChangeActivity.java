package com.android.wannasing.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.wannasing.R;
import com.android.wannasing.profile.ProfileChangePopup;

public class ProfileChangeActivity extends AppCompatActivity {

    ImageButton local,genre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);
        local =  findViewById(R.id.Profchange_ib_local);
        genre =  findViewById(R.id.profchange_ib_genre);
        click();

    }

private void click() {
    local.setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ProfileChangePopup.class);
            intent.putExtra("option", "활동지역 변경");
            startActivity(intent);
        }

    });
    genre.setOnClickListener(new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ProfileChangePopup.class);
            intent.putExtra("option", "선호장르 변경");
            startActivity(intent);
        }

    });




}




}
