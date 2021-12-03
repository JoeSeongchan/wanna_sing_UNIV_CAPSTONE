package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.common.model.old.Party;
import com.android.wannasing.common.viewcontroller.old.DbTrackerLifeCycleManager;
import com.android.wannasing.common.viewcontroller.old.ServerDb;
import com.android.wannasing.common.viewcontroller.old.ServerDbLifeCycleManager;
import com.android.wannasing.common.viewcontroller.old.TransactionManager;
import com.android.wannasing.databinding.ActivityChatGroupBinding;
import com.android.wannasing.feature.chat.showchat.viewcontroller.ChatActivity;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.feature.chat.showchatgroup.view.BriefChatGroupAdapter;
import com.android.wannasing.feature.chat.showchatgroup.view.BriefChatGroupAdapter.BriefChatGroupDiff;
import java.util.ArrayList;
import java.util.Optional;

public class ChatGroupActivity extends AppCompatActivity {

  private String myNick;
  private RecyclerView rv;
  private BriefChatGroupAdapter adapter;

  private ServerDb<Party> partyServerDb;
  private ServerDb<ChatGroup> chatGroupServerDb;
  private TransactionManager transactionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unpackIntent();
    setUi();
    setDb();
  }

  private void unpackIntent() {
    Bundle bundle = getIntent().getExtras();
    this.myNick = Optional.ofNullable(bundle.getString("USER_NICK")).orElse("dummy_user_nick");
  }

  private void setUi() {
    ActivityChatGroupBinding binding = ActivityChatGroupBinding
        .inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // 사용자 닉네임 표시.
    binding.tvNickChatGroup.setText(getString(R.string.tv_nick_chatGroup, myNick));

    // recycler view 설정.
    this.rv = binding.rvChatListChat;
    this.adapter = new BriefChatGroupAdapter(new BriefChatGroupDiff(), this::startChatAct);
    this.rv.setAdapter(this.adapter);
    this.rv.setLayoutManager(new LinearLayoutManager(this));
    this.rv.setItemAnimator(null);
    setSwipe();
  }

  private void startChatAct(String groupId) {
    Intent intent = new Intent(this, ChatActivity.class);
    intent.putExtra("GROUP_ID", groupId);
    intent.putExtra("USER_NICK", myNick);
    startActivity(intent);
  }

  private void setSwipe() {
    ItemTouchHelper.SimpleCallback swipeClbk = new ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT) {

      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder,
          @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
          transactionManager.run(transaction -> {
            chatGroupServerDb.deleteData(adapter.getItem(position), transaction);
            partyServerDb.deleteData(adapter.getItem(position).getPrimaryKey(), transaction);
            return null;
          });
        }
      }

      @Override
      public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
          boolean isCurrentlyActive) {
        Paint paint = new Paint();
        Drawable icon;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
          View itemView = viewHolder.itemView;
          int height = itemView.getBottom() - itemView.getTop();
          int width = itemView.getRight() - itemView.getLeft();
          float iconH = getResources().getDisplayMetrics().density * 28;
          float iconW = getResources().getDisplayMetrics().density * 28;

          if (dX < 0) {
            RectF background = new RectF((float) itemView.getRight() + dX,
                (float) itemView.getTop(), (float) itemView.getRight(),
                (float) itemView.getBottom());
            paint.setColor(Color.parseColor("#E91E63"));
            canvas.drawRect(background, paint);

            icon = ContextCompat.getDrawable(getBaseContext(), android.R.drawable.ic_menu_delete);
            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

            float rate = Math.abs(dX) / width;

            int iconLeft = (int) (itemView.getRight() - width / 3 * rate);
            int iconTop = (int) (itemView.getTop() + height / 2 - iconH / 2);
            int iconRight = (int) (itemView.getRight() + iconW - width / 3 * rate);
            int iconBottom = (int) (itemView.getBottom() - height / 2 + iconH / 2);
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.draw(canvas);
          }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState,
            isCurrentlyActive);
      }
    };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeClbk);
    itemTouchHelper.attachToRecyclerView(this.rv);
  }

  public void setDb() {
    // livedata.
    BriefChatGroupViewModel model = new ViewModelProvider(this).get(BriefChatGroupViewModel.class);
    model.getAll().observe(this,
        briefChatGroups -> this.adapter.submitList(briefChatGroups));

    // TransactionManager 관리.
    this.transactionManager = new TransactionManager();

    // ServerDb 관리.
    ServerDbLifeCycleManager serverDbLifeCycleManager = new ServerDbLifeCycleManager();
    getLifecycle().addObserver(serverDbLifeCycleManager);
    chatGroupServerDb = new ServerDb<>("user_list/" + myNick + "/chat_group_list/",
        ChatGroup.class);
    partyServerDb = new ServerDb<>("chat_group_list/", Party.class);
    serverDbLifeCycleManager.add(chatGroupServerDb);
    serverDbLifeCycleManager.add(partyServerDb);

    // DbTracker 관리.
    DbTrackerLifeCycleManager dbTrackerLifeCycleManager = new DbTrackerLifeCycleManager();
    getLifecycle().addObserver(dbTrackerLifeCycleManager);
    BriefChatGroupDbTracker briefChatGroupDbTracker = new BriefChatGroupDbTracker(
        "user_list/" + myNick + "/chat_group_list/",
        getApplication());
    ChatGroupDbTracker chatGroupDbTracker = new ChatGroupDbTracker("chat_group_list/",
        getApplication());
    briefChatGroupDbTracker.getUpdateInRealTime();
    chatGroupDbTracker.getUpdateInRealTime();
    dbTrackerLifeCycleManager.add(briefChatGroupDbTracker);
    dbTrackerLifeCycleManager.add(chatGroupDbTracker);
  }

  // group 추가하는 함수.
  public void addGroup(View v) {
    Party newParty = new Party("dum01", "dum01", 10, "dum01", "dum01");
    transactionManager.run(transaction -> {
      partyServerDb.setData(newParty, transaction);
      ChatGroup newChatGroup = new ChatGroup(newParty);
      chatGroupServerDb.setData(newChatGroup, transaction);
      return null;
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    adapter.submitList(new ArrayList<>());
  }
}