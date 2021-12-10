package com.android.wannasing.feature.home.viewcontroller;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.wannasing.R;
import com.android.wannasing.common.model.User;
import com.android.wannasing.databinding.ActivityHomeBinding;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.ShowChatGroupFragment;
import com.android.wannasing.feature.map.viewcontroller.MapFragment;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.feature.party.showpartygroup.viewcontroller.ShowPartyGroupFragment;
import com.android.wannasing.feature.party.showpartyinfo.viewcontroller.ShowPartyInfoActivity;
import com.android.wannasing.feature.profile.showmyprofile.viewcontroller.ShowMyProfileFragment;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

  public static final String FROM_LOGIN_ACTIVITY_USER_INFO_DATA_TAG = "FROM_LOGIN_ACTIVITY_USER_INFO_DATA_TAG";
  public static final String FROM_SHOW_CHAT_GROUP_FRAG_TAG = "FROM_SHOW_CHAT_GROUP_FRAG_TAG";
  public static final String FROM_SHOW_PARTY_GROUP_FRAG_TAG = "PARTY_GROUP";
  // fragment.
  private MapFragment mapFragment;
  private ShowPartyGroupFragment showPartyGroupFragment;
  private ShowMyProfileFragment showMyProfileFragment;
  private ShowChatGroupFragment showChatGroupFragment;
  private List<Fragment> allFragmentList;
  private FragmentManager fragmentManager;
  private ActivityHomeBinding binding;
  // Login Activity 로부터 받은 유저 데이터.
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityHomeBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());
    RxJavaPlugins.setErrorHandler(e -> {
      if (e instanceof UndeliverableException) {
        // Merely log undeliverable exceptions
        Utilities.log(LogType.w, "err : " + e.getMessage());
      }
    });

    getUserInfoFromLoginActivity();
    setUpFragment();
  }

  private void setUpFragment() {
    fragmentManager = getSupportFragmentManager();
    showPartyGroupFragment = ShowPartyGroupFragment.newInstance();

    showPartyGroupFragment = ShowPartyGroupFragment.newInstance();
    mapFragment = MapFragment.newInstance(user.getId());
    showChatGroupFragment = ShowChatGroupFragment.newInstance(user);
    showMyProfileFragment = ShowMyProfileFragment.newInstance(user);
    allFragmentList = Arrays
        .asList(showPartyGroupFragment, mapFragment, showChatGroupFragment,
            showMyProfileFragment);
    allFragmentList.forEach(fragment -> fragmentManager.beginTransaction()
        .add(R.id.manageFragments_fcv_fragmentContainer, fragment).commit());

    showFragment(showPartyGroupFragment);

    setUpBottomNav();
    setFragmentResultListener();
  }

  private void setFragmentResultListener() {
    // 파티 리스트에서 특정 아이템 선택한 경우, 파티 세부 정보 Activity 로 전환.
    getSupportFragmentManager()
        .setFragmentResultListener(FROM_SHOW_PARTY_GROUP_FRAG_TAG, this, (requestKey, result) -> {
          Party party = (Party) result
              .getSerializable(ShowPartyInfoActivity.FROM_SHOW_PARTY_GROUP_FRAG_PARTY_DATA_TAG);
          Intent intent = new Intent(this, ShowPartyInfoActivity.class);
          Bundle bundle = new Bundle();
          bundle.putSerializable(ShowPartyInfoActivity.FROM_SHOW_PARTY_GROUP_FRAG_PARTY_DATA_TAG,
              party);
          bundle.putString(ShowPartyInfoActivity.FROM_HOME_ACTIVITY_USER_ID_TAG, user.getId());
          intent.putExtras(bundle);
          startActivity(intent);
        });
  }

  private void getUserInfoFromLoginActivity() {
    this.user = (User) getIntent().getSerializableExtra(FROM_LOGIN_ACTIVITY_USER_INFO_DATA_TAG);
  }

  private void setUpBottomNav() {
    // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
    binding.manageFragmentsBnvBottomNavi.setOnItemSelectedListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.home_nav:
              showFragment(showPartyGroupFragment);
              break;
            case R.id.map_nav:
              showFragment(mapFragment);
              break;
            case R.id.prof_nav:
              showFragment(showMyProfileFragment);
              break;
            case R.id.chat_nav:
              showFragment(showChatGroupFragment);
              break;
          }
          return true;
        }
    );
  }

  private void showFragment(Fragment fragmentToShow) {
    allFragmentList
        .forEach(fragment -> fragmentManager.beginTransaction().hide(fragment).commit());
    fragmentManager.beginTransaction().show(fragmentToShow).commit();
  }

}
