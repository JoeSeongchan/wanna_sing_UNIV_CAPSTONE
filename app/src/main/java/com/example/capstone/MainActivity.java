/*이슬코드에서 따옴. 테스트용으로 사용중. 참고 후 업애도 됨 */



package com.example.capstone;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.capstone.HomeFragment;
import com.example.capstone.R;

import com.example.capstone.ShowMyProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    //private Menu1Fragment menu1Fragment = new Menu1Fragment();
    private ShowMyProfileFragment menu4Fragment =new ShowMyProfileFragment();
    private HomeFragment menu1Fragment =new HomeFragment();
    //private Menu3Fragment menu3Fragment = new Menu3Fragment();
    //private Menu4Fragment menu4Fragment = new Menu4Fragment();


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
                        switch (item.getItemId()) {
                            case R.id.home_nav: {
                                transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                                break;
                            }
                        }
                        switch (item.getItemId()) {
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