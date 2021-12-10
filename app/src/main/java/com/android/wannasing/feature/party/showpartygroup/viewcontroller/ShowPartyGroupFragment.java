package com.android.wannasing.feature.party.showpartygroup.viewcontroller;

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
import com.android.wannasing.feature.home.viewcontroller.HomeActivity;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.feature.party.showpartygroup.viewcontroller.ShowPartyGroupAdapter.PartyDiff;
import com.android.wannasing.feature.party.showpartygroup.viewcontroller.ShowPartyGroupViewHolder.OnPartyItemClickListener;
import com.android.wannasing.feature.party.showpartygroup.viewmodel.PartyGroupInHomeViewModel;
import com.android.wannasing.feature.party.showpartygroup.viewmodel.PartyGroupInHomeViewModelFactory;
import com.android.wannasing.feature.party.showpartyinfo.viewcontroller.ShowPartyInfoActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowPartyGroupFragment extends Fragment implements
    OnPartyItemClickListener {

  public static final String TAG = "HomeActivity_R";
  private static final int ITEM_SHOW_LIMIT = 5;

  //리사이클러 뷰 관련
  private boolean isScrolling = false;
  private boolean isLastItemReached = false;

  private RecyclerView rv;
  private ShowPartyGroupAdapter adapter;
  private PartyGroupInHomeViewModel viewModel;
  private View rootView;

  public ShowPartyGroupFragment() {
  }

  public static ShowPartyGroupFragment newInstance() {
    ShowPartyGroupFragment instance = new ShowPartyGroupFragment();
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
    return inflater.inflate(R.layout.fragment_show_party_group, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    rootView = view;
    setRecyclerView();
  }

  private void setRecyclerView() {
    rv = rootView.findViewById(R.id.showPartyGroup_rv_partyList);
    adapter = new ShowPartyGroupAdapter(new PartyDiff(), this);
    rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    rv.setAdapter(adapter);
    PartyGroupInHomeViewModelFactory factory = new PartyGroupInHomeViewModelFactory(
        FirebaseFirestore.getInstance());
    viewModel = new ViewModelProvider(this, factory).get(PartyGroupInHomeViewModel.class);
    viewModel.getPartyList().observe(getActivity(), partyList -> adapter.submitList(partyList));
  }

  @Override
  public void onPartyItemClick(Party party) {
    informActivityWhichPartyIsClicked(party);
  }

  private void informActivityWhichPartyIsClicked(Party party) {
    Bundle result = new Bundle();
    result.putSerializable(ShowPartyInfoActivity.FROM_SHOW_PARTY_GROUP_FRAG_PARTY_DATA_TAG, party);
    getParentFragmentManager()
        .setFragmentResult(HomeActivity.FROM_SHOW_PARTY_GROUP_FRAG_TAG, result);
  }
}
