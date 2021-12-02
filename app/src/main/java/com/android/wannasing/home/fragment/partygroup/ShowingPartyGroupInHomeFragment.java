package com.android.wannasing.home.fragment.partygroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.db.entity.Party;
import com.android.wannasing.home.fragment.partygroup.recyclerview.adapter.PartyInHomeAdapter;
import com.android.wannasing.home.fragment.partygroup.recyclerview.adapter.PartyInHomeAdapter.PartyDiff;
import com.android.wannasing.home.fragment.partygroup.recyclerview.viewholder.PartyInHomeViewHolder.OnPartyItemClickListener;
import com.android.wannasing.home.fragment.partygroup.viewmodel.PartyGroupInHomeViewModel;
import com.android.wannasing.home.fragment.partygroup.viewmodel.PartyGroupInHomeViewModelFactory;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowingPartyGroupInHomeFragment extends Fragment implements
    OnPartyItemClickListener {

  public static final String TAG = "HomeActivity_R";
  private static final int ITEM_SHOW_LIMIT = 5;

  //리사이클러 뷰 관련
  private boolean isScrolling = false;
  private boolean isLastItemReached = false;

  private RecyclerView rv;
  private PartyInHomeAdapter adapter;
  private PartyGroupInHomeViewModel viewModel;
  private View rootView;

  public ShowingPartyGroupInHomeFragment() {
  }

  public static ShowingPartyGroupInHomeFragment newInstance() {
    ShowingPartyGroupInHomeFragment instance = new ShowingPartyGroupInHomeFragment();
    return instance;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_showing_party_group_in_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    rootView = view;
    setRecyclerView();
  }

  private void setRecyclerView() {
    rv = rootView.findViewById(R.id.main_rv_partyList);
    adapter = new PartyInHomeAdapter(new PartyDiff(), this);
    rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    rv.setAdapter(adapter);
    PartyGroupInHomeViewModelFactory factory = new PartyGroupInHomeViewModelFactory(
        FirebaseFirestore.getInstance(), ITEM_SHOW_LIMIT);
    viewModel = new ViewModelProvider(this, factory).get(PartyGroupInHomeViewModel.class);
    viewModel.getPartyList().observe(getActivity(), partyList -> adapter.submitList(partyList));
  }

  @Override
  public void onPartyItemClick(Party party) {
    informActivityWhichPartyIsClicked(party);
  }

  private void informActivityWhichPartyIsClicked(Party party) {
    Bundle result = new Bundle();
    result.putSerializable("PARTY_INFO", party);
    getParentFragmentManager()
        .setFragmentResult("PARTY_GROUP", result);
  }
}
