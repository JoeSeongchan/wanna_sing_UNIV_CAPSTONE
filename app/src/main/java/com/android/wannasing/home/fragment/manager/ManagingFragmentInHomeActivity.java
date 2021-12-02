package com.android.wannasing.home.fragment.manager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.wannasing.R;
import com.android.wannasing.databinding.ActivityManagingFragmentInHomeBinding;
import com.android.wannasing.db.entity.Party;
import com.android.wannasing.home.fragment.map.ManagingMapFragment;
import com.android.wannasing.home.fragment.partygroup.ShowingPartyGroupInHomeFragment;
import com.android.wannasing.party.showinginfo.ShowingPartyInfoActivity;

public class ManagingFragmentInHomeActivity extends AppCompatActivity {

  private ManagingMapFragment managingMapFragment;
  private ShowingPartyGroupInHomeFragment showingPartyGroupInHomeFragment;
  private FragmentManager fragmentManager;

  private ActivityManagingFragmentInHomeBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityManagingFragmentInHomeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    fragmentManager = getSupportFragmentManager();
    managingMapFragment = ManagingMapFragment.newInstance();
    showingPartyGroupInHomeFragment = ShowingPartyGroupInHomeFragment.newInstance();

    // 첫 화면 지정
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.manageFragments_fcv_fragmentContainer, showingPartyGroupInHomeFragment)
        .commitAllowingStateLoss();
    setBottomNav();

    getSupportFragmentManager()
        .setFragmentResultListener("PARTY_GROUP", this, (requestKey, result) -> {
          Party party = (Party) result.getSerializable("PARTY_INFO");
          Intent intent = new Intent(this, ShowingPartyInfoActivity.class);
          Bundle bundle = new Bundle();
          bundle.putSerializable("SELECTED_PARTY", party);
          intent.putExtras(bundle);
          startActivity(intent);
        });
  }

  private void setBottomNav() {
    // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
    binding.manageFragmentsBnvBottomNavi.setOnItemSelectedListener(
        item -> {
          FragmentTransaction transaction = fragmentManager.beginTransaction();
          switch (item.getItemId()) {
            case R.id.home_nav:
              transaction.replace(R.id.manageFragments_fcv_fragmentContainer,
                  showingPartyGroupInHomeFragment).commit();
              break;
            case R.id.map_nav:
            default:
              transaction.replace(R.id.manageFragments_fcv_fragmentContainer,
                  managingMapFragment).commit();
              break;
          }
          return true;
        }
    );
  }

}
