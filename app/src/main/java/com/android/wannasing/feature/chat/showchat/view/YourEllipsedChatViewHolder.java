package com.android.wannasing.feature.chat.showchat.view;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.wannasing.R;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import java.text.SimpleDateFormat;

public class YourEllipsedChatViewHolder extends ViewHolder implements ChatBindable {


  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
  private final View root;
  private final TextView tvNick, tvMsg, tvDate;
  private final Button btnFullText;
  private Chat chat;

  public YourEllipsedChatViewHolder(@NonNull View itemView) {
    super(itemView);
    root = itemView;
    tvNick = root.findViewById(R.id.tv_nickName_yourElChat);
    tvMsg = root.findViewById(R.id.tv_msg_yourElChat);
    tvDate = root.findViewById(R.id.tv_date_yourElChat);
    btnFullText = itemView.findViewById(R.id.btn_fullText_yourElChat);
    int DISPLAY_WIDTH_PIXEL = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
    setWidth(DISPLAY_WIDTH_PIXEL / 2);
  }

  public static YourEllipsedChatViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.your_chat_ellipsed, parent, false);
    return new YourEllipsedChatViewHolder(view);
  }

  public String getFullMsg() {
    if (chat == null) {
      return "";
    }
    return chat.getMsg();
  }

  public String getEllipsedMsg() {
    if (chat == null) {
      return "";
    }
    return chat.getMsg().substring(0, 29) + "...";
  }

  public void setBtnClickLis(View.OnClickListener clickListener) {
    btnFullText.setOnClickListener(clickListener);
  }

  public void bind(@NonNull Chat chat) {
    this.chat = chat;
    this.tvNick.setText(chat.getNickname());
    this.tvMsg.setText(getEllipsedMsg());
    this.tvDate.setText(ChatUtilities.makeDateStr(chat));
  }

  public void setWidth(final int width) {
    this.tvMsg.setWidth(width);
  }
}
