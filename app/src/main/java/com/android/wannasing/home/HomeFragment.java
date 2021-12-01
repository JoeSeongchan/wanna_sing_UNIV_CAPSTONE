package com.android.wannasing.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.wannasing.R;
import com.android.wannasing.databinding.ActivityHomeBinding;
import com.android.wannasing.db.entity.Party;
import com.android.wannasing.home.recyclerview.adapter.HomePartyAdapter;
import com.android.wannasing.home.recyclerview.adapter.HomePartyAdapter.HomePartyDiff;
import com.android.wannasing.home.recyclerview.viewholder.HomePartyViewHolder.OnPartyItemClickListener;
import com.android.wannasing.showpartyinfo.ShowPartyInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment implements OnPartyItemClickListener {

  public static final String TAG = "HomeActivity_R";
  private static final int ITEM_SHOW_LIMIT = 5;
  // 여기서부터는 데이터베이스
  protected FirebaseFirestore db = FirebaseFirestore.getInstance();
  private BottomNavigationView bottomNav; // 바텀 네비게이션 뷰
  private FragmentManager fm;
  private FragmentTransaction ft;

  //리사이클러 뷰 관련
  private boolean isScrolling = false;
  private boolean isLastItemReached = false;
  private DocumentSnapshot lastSeenParty;

  private RecyclerView rv;
  private HomePartyAdapter adapter;
  private ActivityHomeBinding binding;
  private PartyViewModel viewModel;


  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_home, container, false);
    binding = ActivityHomeBinding.inflate(getLayoutInflater());
    setRecyclerView();
    return v;
  }


  private void setRecyclerView() {
    rv = binding.mainRvPartyList;
    adapter = new HomePartyAdapter(new HomePartyDiff(), this);
    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    rv.setAdapter(adapter);
    PartyViewModelFactory factory = new PartyViewModelFactory(FirebaseFirestore.getInstance(), 5);
    viewModel = new ViewModelProvider((getActivity()), factory).get(PartyViewModel.class);
    viewModel.getPartyList().observe((getActivity()), partyList -> adapter.submitList(partyList));
    setRecyclerViewScollManager();
  }

  private void setRecyclerViewScollManager() {
    //스크롤 리스터
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView rv, int newState) {
        super.onScrollStateChanged(rv, newState);
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
          isScrolling = true;
        }
      }

      @Override
      public void onScrolled(RecyclerView rv, int dx, int dy) {
        super.onScrolled(rv, dx, dy);
        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) rv
            .getLayoutManager());
        int firstVisibleItemPosition = linearLayoutManager
            .findFirstVisibleItemPosition();
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        // 사용자가 밑으로 스크롤 -> 시스템에게 업데이트 신호 보냄.
        if (isScrolling && (firstVisibleItemPosition + visibleItemCount
            == totalItemCount) && !isLastItemReached) {
          Log.d(TAG, "onScrolled: ");
          isScrolling = false; // 필드명 수정
          viewModel.retrieveNextPartyList();
        }
      }
    };
    rv.addOnScrollListener(onScrollListener);
  }

  @Override
  public void onPartyItemClick(Party party) {
    Toast.makeText((getActivity()),
        String.format("party item clicked : %1$s", party.partyName), Toast.LENGTH_SHORT).show();
    Intent intent = new Intent((getActivity()) , ShowPartyInfoActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("PARTY_INFO", party);
    intent.putExtras(bundle);
    startActivity(intent);
  }
}
