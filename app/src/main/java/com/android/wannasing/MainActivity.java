package com.android.wannasing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.creategroup.CreateGroupActivity;

public class MainActivity extends AppCompatActivity {

  Button test, test2;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    test = findViewById(R.id.TESTHOME);
    test2 = findViewById(R.id.TEST2);
    test.setOnClickListener(this::onClick);
    test2.setOnClickListener(this::onClick);

  }

  public void onClick(View view) {
    if (view == test) {
      Intent intent = new Intent(getApplicationContext(), CreateGroupActivity.class);
      startActivity(intent);
    }

    if (view == test2) {
      Intent intent = new Intent(getApplicationContext(), GroupDetail.class);
      startActivity(intent);
    }
  }

}