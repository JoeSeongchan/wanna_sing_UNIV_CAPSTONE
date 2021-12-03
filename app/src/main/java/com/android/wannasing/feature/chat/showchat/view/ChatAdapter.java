package com.android.wannasing.feature.chat.showchat.view;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;

public class ChatAdapter extends ListAdapter<Chat, ViewHolder> {

  private static final int TYPE_MY_CHAT = 0;
  private static final int TYPE_YOUR_CHAT = 1;
  private static final int TYPE_MY_CHAT_ELLIPSED = 2;
  private static final int TYPE_YOUR_CHAT_ELLIPSED = 3;
  private static final int MSG_MAX_LENGTH = 30;


  private final String myNick;

  public ChatAdapter(
      @NonNull DiffUtil.ItemCallback<Chat> diffCallback, @NonNull String myNick) {
    super(diffCallback);
    this.setHasStableIds(true);
    this.myNick = myNick;       // 닉네임 전달.
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_MY_CHAT:
        return MyChatViewHolder.create(parent);
      case TYPE_YOUR_CHAT:
        return YourChatViewHolder.create(parent);
      case TYPE_MY_CHAT_ELLIPSED:
        return MyEllipsedChatViewHolder.create(parent);
      case TYPE_YOUR_CHAT_ELLIPSED:
      default:
        return YourEllipsedChatViewHolder.create(parent);
    }
  }

  @Override
  public int getItemViewType(int position) {
    Chat item = getItem(position);
    if (item.getNickname().compareTo(myNick) == 0) {
      if (isEllipsed(item.getMsg())) {
        return TYPE_MY_CHAT_ELLIPSED;
      } else {
        return TYPE_MY_CHAT;
      }
    } else {
      if (isEllipsed(item.getMsg())) {
        return TYPE_YOUR_CHAT_ELLIPSED;
      } else {
        return TYPE_YOUR_CHAT;
      }
    }
  }

  private boolean isEllipsed(@NonNull String msg) {
    if (msg.length() <= MSG_MAX_LENGTH) {
      return false;
    }
    return true;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    assert holder instanceof ChatBindable;
    ((ChatBindable) holder).bind(getItem(position));

    // 클릭 리스너 지정.
    if (holder instanceof MyEllipsedChatViewHolder) {
      MyEllipsedChatViewHolder ellipsed = (MyEllipsedChatViewHolder) holder;
      ellipsed.setBtnClickLis(view -> {
        Utilities.log(LogType.d, "full text : " + ellipsed.getFullMsg());
      });
    }
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  @Override
  public Chat getItem(int position) {
    return super.getItem(position);
  }

  public static class ChatDiff extends ItemCallback<Chat> {

    @Override
    public boolean areItemsTheSame(@NonNull Chat oldItem,
        @NonNull Chat newItem) {
      return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Chat oldItem,
        @NonNull Chat newItem) {
      return oldItem.equals(newItem);
    }
  }
}
