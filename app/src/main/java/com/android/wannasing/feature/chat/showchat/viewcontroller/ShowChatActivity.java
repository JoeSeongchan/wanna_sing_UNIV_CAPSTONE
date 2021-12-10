package com.android.wannasing.feature.chat.showchat.viewcontroller;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.common.viewcontroller.FireDb.TransactionManager;
import com.android.wannasing.databinding.ActivityChatBinding;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import com.android.wannasing.feature.chat.showchat.viewcontroller.ChatAdapter.ChatDiff;
import com.android.wannasing.feature.chat.showchat.viewmodel.ChatViewModel;
import com.android.wannasing.old.firedb.ServerDb;
import java.util.ArrayList;

public class ShowChatActivity extends AppCompatActivity {

  public static final String FROM_SHOW_CHAT_GROUP_FRAG_PARTY_ID_TAG = "FROM_SHOW_CHAT_GROUP_FRAG_PARTY_ID_TAG";
  public static final String FROM_SHOW_CHAT_GROUP_FRAG_MY_NICK_TAG = "FROM_SHOW_CHAT_GROUP_FRAG_MY_NICK_TAG";

  private String myNick;
  private String partyId;
  private RecyclerView rv;
  private ChatAdapter adapter;
  private TransactionManager transactionManager;
  private ServerDb<Chat> chatServerDb;
  private EditText etMsg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unpackIntent();
    setupUI();
    setupDb();
  }

  private void unpackIntent() {
    Bundle bundle = getIntent().getExtras();
    this.partyId = bundle.getString(FROM_SHOW_CHAT_GROUP_FRAG_PARTY_ID_TAG);
    this.myNick = bundle.getString(FROM_SHOW_CHAT_GROUP_FRAG_MY_NICK_TAG);
  }

  // ui 설정하는 함수.
  private void setupUI() {
    // 접근 권한 코드.
    ActivityChatBinding binding = ActivityChatBinding
        .inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    etMsg = binding.etMsgChat;

    // enter 입력 시 처리 방안.
    etMsg.setOnKeyListener((v, keyCode, event) -> {
      if (keyCode == KeyEvent.KEYCODE_ENTER
          && event.getAction() == KeyEvent.ACTION_DOWN) {
        sendMsg(binding.buttonSend);
        return true;
      }
      return false;
    });

    // recycler view 세팅.
    rv = findViewById(R.id.recyclerView);
    adapter = new ChatAdapter(new ChatDiff(), myNick);
    rv.setAdapter(adapter);
    rv.setLayoutManager(new LinearLayoutManager(this));
    this.rv.setItemAnimator(null);

    setSwipe();
  }

  // recycler view 아이템 스와이프 리스너 설정하는 함수.
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
            chatServerDb.deleteData(adapter.getItem(position), transaction);
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

  // db 설정하는 함수.
  public void setupDb() {
    ChatViewModel model = new ViewModelProvider(this).get(ChatViewModel.class);
    model.getAll().observe(this,
        chatList -> this.adapter.submitList(chatList));

    // TransactionManager.
    transactionManager = new TransactionManager();

    // ServerDb 설정.
    ServerDbLifeCycleManager serverDbLifeCycleManager = new ServerDbLifeCycleManager();
    getLifecycle().addObserver(serverDbLifeCycleManager);
    chatServerDb = new ServerDb<>("chat_group_list/" + partyId + "/chat_list", Chat.class);
    serverDbLifeCycleManager.add(chatServerDb);

    // DbTracker 설정.
    DbTrackerLifeCycleManager dbTrackerLifeCycleManager = new DbTrackerLifeCycleManager();
    getLifecycle().addObserver(dbTrackerLifeCycleManager);
    ChatDbTracker chatDbTracker = new ChatDbTracker(
        partyId,
        "chat_group_list/" + partyId + "/chat_list",
        getApplication());
    chatDbTracker.getUpdateInRealTime();
    dbTrackerLifeCycleManager.add(chatDbTracker);
  }

  // 사용자가 입력한 메시지를 firebase firestore 서버로 보내는 함수.
  public void sendMsg(View view) {
    String msg = etMsg.getText().toString();
    if (msg.length() == 0) {
      return;
    }
    Chat item = new Chat(partyId, myNick, msg);
    transactionManager.run(transaction -> {
      chatServerDb.setData(item, transaction);
      return null;
    });
    etMsg.setText("");
  }

  @Override
  protected void onDestroy() {
    adapter.submitList(new ArrayList<>());
    super.onDestroy();
  }
}