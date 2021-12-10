package com.android.wannasing.feature.chat.showchat.view.noellipsed;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.wannasing.R;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import com.android.wannasing.feature.chat.showchat.view.ChatBindable;
import com.android.wannasing.feature.chat.showchat.view.ChatUtilities;
import java.text.SimpleDateFormat;

public class YourChatViewHolder extends ViewHolder implements ChatBindable {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

  private final View root;
  private final TextView tvNick, tvMsg, tvDate;

  public YourChatViewHolder(@NonNull View itemView) {
    super(itemView);
    root = itemView;
    tvNick = root.findViewById(R.id.tv_nickName_yourChat);
    tvMsg = root.findViewById(R.id.tv_msg_yourChat);
    tvDate = root.findViewById(R.id.tv_date_yourChat);
  }

  public static YourChatViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_chat, parent, false);
    return new YourChatViewHolder(view);
  }

  public void bind(@NonNull Chat chat) {
    this.tvNick.setText(chat.getNickname());
    this.tvMsg.setText(chat.getMsg());
    this.tvDate.setText(ChatUtilities.makeDateStr(chat));
  }
}
