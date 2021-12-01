package com.example.detailgrouptest;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.detailgrouptest.map.FragmentMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private FragmentManager fragmentManager = getSupportFragmentManager();
  // 4개의 메뉴에 들어갈 Fragment들
  //private Menu1Fragment menu1Fragment = new Menu1Fragment();
  private FragmentMap menu2Fragment = new FragmentMap();
  //private Menu3Fragment menu3Fragment = new Menu3Fragment();
  //private Menu4Fragment menu4Fragment = new Menu4Fragment();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView bottomNavigationView = findViewById(R.id.main_bnv_bottomNavi);

    // 첫 화면 지정
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();

    // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
              case R.id.home_nav: {
                transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                break;
              }
              case R.id.map_nav: {
                transaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
                break;
              }
              case R.id.chat_nav: {
                transaction.replace(R.id.frame_layout, menu3Fragment).commitAllowingStateLoss();
                break;
              }
              case R.id.prof_nav: {
                transaction.replace(R.id.frame_layout, menu4Fragment).commitAllowingStateLoss();
                break;
              }
            }

            return true;
          }
        });
  }

}