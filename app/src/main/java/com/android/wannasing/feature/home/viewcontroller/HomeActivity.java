package com.android.wannasing.feature.home.viewcontroller;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.wannasing.R;
import com.android.wannasing.common.model.User;
import com.android.wannasing.databinding.ActivityHomeBinding;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.ChatGroupActivity;
import com.android.wannasing.feature.map.viewcontroller.ManagingMapFragment;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.feature.party.showpartygroup.viewcontroller.ShowPartyGroupFragment;
import com.android.wannasing.feature.party.showpartyinfo.viewcontroller.ShowPartyInfoActivity;
import com.android.wannasing.feature.profile.showmyprofile.viewcontroller.ShowMyProfileFragment;

public class HomeActivity extends AppCompatActivity {

  // fragment.
  private ManagingMapFragment managingMapFragment;
  private ShowPartyGroupFragment showPartyGroupFragment;
  private ShowMyProfileFragment showMyProfileFragment;

  private FragmentManager fragmentManager;

  private ActivityHomeBinding binding;

  // Login Activity 로부터 받은 유저 데이터.
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityHomeBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());
    getUserInfoFromLoginActivity();
    setUpFragment();
  }

  private void setUpFragment() {
    fragmentManager = getSupportFragmentManager();
    showPartyGroupFragment = ShowPartyGroupFragment.newInstance();

    // 첫 화면 : 파티 리스트 보여주는 fragment.
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.manageFragments_fcv_fragmentContainer, showPartyGroupFragment)
        .commitAllowingStateLoss();

    setUpBottomNav();
    setFragmentResultListener();
  }

  private void setFragmentResultListener() {
    // 파티 리스트에서 특정 아이템 선택한 경우, 파티 세부 정보 Activity 로 전환.
    getSupportFragmentManager()
        .setFragmentResultListener("PARTY_GROUP", this, (requestKey, result) -> {
          Party party = (Party) result.getSerializable("SELECTED_PARTY_INFO");
          Intent intent = new Intent(this, ShowPartyInfoActivity.class);
          Bundle bundle = new Bundle();
          bundle.putSerializable("SELECTED_PARTY_INFO", party);
          intent.putExtras(bundle);
          startActivity(intent);
        });
  }

  private void getUserInfoFromLoginActivity() {
    this.user = (User) getIntent().getSerializableExtra("USER_INFO");
  }

  private void setUpBottomNav() {
    // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
    binding.manageFragmentsBnvBottomNavi.setOnItemSelectedListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.home_nav:
              showFragment(FragmentType.SHOW_PARTY_GROUP);
              break;
            case R.id.map_nav:
              showFragment(FragmentType.MANAGE_MAP);
              break;
            case R.id.prof_nav:
              showFragment(FragmentType.SHOW_MY_PROFILE);
              break;
            case R.id.chat_nav:
              Intent intent = new Intent(this, ChatGroupActivity.class);
              intent.putExtra("USER_NICK", user.getNick());
              intent.putExtra("GROUP_ID", "dummy_group_id");
              startActivity(intent);
          }
          return true;
        }
    );
  }

  private void showFragment(FragmentType fragmentType) {
    switch (fragmentType) {
      case SHOW_PARTY_GROUP:
        if (showPartyGroupFragment == null) {
          showPartyGroupFragment = ShowPartyGroupFragment.newInstance();
          fragmentManager.beginTransaction()
              .add(R.id.manageFragments_fcv_fragmentContainer, showPartyGroupFragment)
              .commit();
        }

        if (showPartyGroupFragment != null) {
          fragmentManager.beginTransaction().show(showPartyGroupFragment).commit();
        }
        if (managingMapFragment != null) {
          fragmentManager.beginTransaction().hide(managingMapFragment).commit();
        }
        if (showMyProfileFragment != null) {
          fragmentManager.beginTransaction().hide(showMyProfileFragment).commit();
        }
        break;
      case MANAGE_MAP:
        if (managingMapFragment == null) {
          managingMapFragment = ManagingMapFragment.newInstance();
          fragmentManager.beginTransaction()
              .add(R.id.manageFragments_fcv_fragmentContainer, managingMapFragment)
              .commit();
        }

        if (showPartyGroupFragment != null) {
          fragmentManager.beginTransaction().hide(showPartyGroupFragment).commit();
        }
        if (managingMapFragment != null) {
          fragmentManager.beginTransaction().show(managingMapFragment).commit();
        }
        if (showMyProfileFragment != null) {
          fragmentManager.beginTransaction().hide(showMyProfileFragment).commit();
        }
        break;
      case SHOW_MY_PROFILE:
        if (showMyProfileFragment == null) {
          showMyProfileFragment = ShowMyProfileFragment.newInstance(user);
          fragmentManager.beginTransaction()
              .add(R.id.manageFragments_fcv_fragmentContainer, showMyProfileFragment)
              .commit();
        }

        if (showPartyGroupFragment != null) {
          fragmentManager.beginTransaction().hide(showPartyGroupFragment).commit();
        }
        if (managingMapFragment != null) {
          fragmentManager.beginTransaction().hide(managingMapFragment).commit();
        }
        if (showMyProfileFragment != null) {
          fragmentManager.beginTransaction().show(showMyProfileFragment).commit();
        }
        break;
    }
  }


  public enum FragmentType {
    SHOW_PARTY_GROUP, MANAGE_MAP, SHOW_MY_PROFILE
  }

}
