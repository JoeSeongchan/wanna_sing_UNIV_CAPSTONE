package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.common.model.User;
import com.android.wannasing.feature.chat.showchat.viewcontroller.ShowChatActivity;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.ShowChatGroupAdapter.ChatGroupDiff;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.ShowChatGroupViewHolder.OnChatGroupItemClickListener;
import com.android.wannasing.feature.chat.showchatgroup.viewmodel.ShowChatGroupViewModel;
import com.android.wannasing.feature.chat.showchatgroup.viewmodel.ShowChatGroupViewModelFactory;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Optional;

public class ShowChatGroupFragment extends Fragment implements
    OnChatGroupItemClickListener {

  public static final String TAG = "HomeActivity_R";
  public static final String ARGS_USER_INFO = "ARGS_USER_INFO";
  //리사이클러 뷰 관련
  private boolean isScrolling = false;
  private boolean isLastItemReached = false;

  private RecyclerView rv;
  private ShowChatGroupAdapter adapter;
  private ShowChatGroupViewModel viewModel;
  private View rootView;
  private User currentUser;

  public ShowChatGroupFragment() {
  }

  public static ShowChatGroupFragment newInstance(User user) {
    ShowChatGroupFragment instance = new ShowChatGroupFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARGS_USER_INFO, user);
    instance.setArguments(args);
    return instance;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Optional<Bundle> arg = Optional.ofNullable(getArguments());
    this.currentUser = arg.map(bundle -> (User) bundle.getSerializable(ARGS_USER_INFO))
        .orElse(User.DUMMY_USER);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_show_chat_group, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    rootView = view;
    ((TextView) rootView.findViewById(R.id.showChatGroup_tv_userNick)).setText(
        rootView.getContext().getString(R.string.showChatGroup_tv_userNick, currentUser.getNick()));
    setRecyclerView();
  }

  private void setRecyclerView() {
    rv = rootView.findViewById(R.id.showChatGroup_rv_container);
    adapter = new ShowChatGroupAdapter(new ChatGroupDiff(), this);
    rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    rv.setAdapter(adapter);
    ShowChatGroupViewModelFactory factory = new ShowChatGroupViewModelFactory(
        FirebaseFirestore.getInstance(), currentUser);
    viewModel = new ViewModelProvider(this, factory).get(ShowChatGroupViewModel.class);
    viewModel.getChatGroupList().observe(getActivity(), partyList -> adapter.submitList(partyList));
  }

  private void informActivityWhichChatGroupIsClicked(ChatGroup chatGroup) {
    Intent intent = new Intent(getActivity(), ShowChatActivity.class);

    intent.putExtra(ShowChatActivity.FROM_SHOW_CHAT_GROUP_FRAG_PARTY_ID_TAG,
        chatGroup.getPrimaryKey());
    intent.putExtra(ShowChatActivity.FROM_SHOW_CHAT_GROUP_FRAG_MY_NICK_TAG,
        currentUser.getNick());
    getActivity().startActivity(intent);
  }

  @Override
  public void onPartyItemClick(ChatGroup chatGroup) {
    informActivityWhichChatGroupIsClicked(chatGroup);
  }
}
