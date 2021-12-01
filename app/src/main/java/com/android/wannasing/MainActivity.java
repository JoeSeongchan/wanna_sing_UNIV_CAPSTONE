/*이슬코드에서 따옴. 테스트용으로 사용중. 참고 후 업애도 됨 */



package com.android.wannasing;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.wannasing.R;
import com.android.wannasing.home.HomeFragment;
import com.android.wannasing.profile.ShowMyProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


  private FragmentManager fragmentManager = getSupportFragmentManager();
  // 4개의 메뉴에 들어갈 Fragment들

  private HomeFragment menu1Fragment =new HomeFragment();
  //private Menu3Fragment menu2Fragment = new Menu2Fragment();
  //private Menu4Fragment menu3Fragment = new Menu3Fragment();
  private ShowMyProfileFragment menu4Fragment =new ShowMyProfileFragment();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);

    // 첫 화면 지정
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();

    // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
    bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) { //홈, 그러니까 검색화면
                  case R.id.home_nav: {
                    transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                    break;
                  }
                }
                switch (item.getItemId()) { //프로필 화면
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