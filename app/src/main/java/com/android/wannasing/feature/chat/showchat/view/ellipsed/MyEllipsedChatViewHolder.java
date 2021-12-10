package com.android.wannasing.feature.chat.showchat.view.ellipsed;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.wannasing.R;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import com.android.wannasing.feature.chat.showchat.view.ChatBindable;
import com.android.wannasing.feature.chat.showchat.view.ChatUtilities;
import java.text.SimpleDateFormat;

public class MyEllipsedChatViewHolder extends ViewHolder implements ChatBindable {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
  private final View root;
  private final TextView tvNick, tvMsg, tvDate;
  private final Button btnFullText;
  private Chat chat;

  public MyEllipsedChatViewHolder(@NonNull View itemView) {
    super(itemView);
    root = itemView;
    tvNick = root.findViewById(R.id.tv_nickName_myElChat);
    tvMsg = root.findViewById(R.id.tv_msg_myElChat);
    tvDate = root.findViewById(R.id.tv_date_myElChat);
    btnFullText = itemView.findViewById(R.id.btn_fullText_myElChat);
    int DISPLAY_WIDTH_PIXEL = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
    setWidth(DISPLAY_WIDTH_PIXEL / 2);
  }

  public static MyEllipsedChatViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.my_chat_ellipsed, parent, false);
    return new MyEllipsedChatViewHolder(view);
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
