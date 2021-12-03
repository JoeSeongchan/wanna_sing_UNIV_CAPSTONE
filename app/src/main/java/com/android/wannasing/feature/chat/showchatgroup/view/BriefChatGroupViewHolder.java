package com.android.wannasing.feature.chat.showchatgroup.view;


import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import java.text.SimpleDateFormat;
import java.util.EventListener;

public class BriefChatGroupViewHolder extends RecyclerView.ViewHolder {

  final static @DrawableRes
  int DEF_REP_PIC = R.drawable.def_rep_pic;
  final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  final private View root;
  //  final private ImageView ivPic;
  final private TextView tvTitle;
  final private TextView tvRecentMsg;
  final private TextView tvDateTime;
  //  final private TextView tvUnReadMsgCount;
  private onItemClickListener onItemClickListener;
  private ChatGroup data;


  public BriefChatGroupViewHolder(View v) {
    super(v);
    root = v;
//    ivPic = v.findViewById(R.id.iv_repPic);
    tvTitle = v.findViewById(R.id.tv_groupTitle);
    tvRecentMsg = v.findViewById(R.id.tv_currentMsg_chatGroup);
    tvDateTime = v.findViewById(R.id.tv_date);
//    tvUnReadMsgCount = v.findViewById(R.id.tv_unReadMsgCount);
    v.setClickable(true);
    v.setEnabled(true);
    root.setOnClickListener(view -> {
      if (data != null) {
        this.onItemClickListener.onClick(data.getId());
      }
    });
  }

  @NonNull
  public static BriefChatGroupViewHolder create(@NonNull ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.chat_group, parent, false);
    return new BriefChatGroupViewHolder(view);
  }

  public void setOnItemClickListener(onItemClickListener lis) {
    this.onItemClickListener = lis;
  }

  public void bind(@NonNull ChatGroup data) {
    this.data = data;
//    setRepPicSrc(R.drawable.def_rep_pic);
    this.tvTitle.setText(data.getTitle());
    this.tvRecentMsg.setText(data.getLastMsg());
    this.tvDateTime.setText(dateFormat.format(data.getCreatedAt().toDate()));
//    this.tvUnReadMsgCount.setText("0");
  }

  // setter for representative picture.
  private void setRepPicSrc(@DrawableRes int id) {
    BitmapDrawable img =
        (BitmapDrawable) ResourcesCompat
            .getDrawable(root.getResources(), DEF_REP_PIC, null);
//    ivPic.setImageDrawable(img);
  }

  public interface onItemClickListener extends EventListener {

    void onClick(String uid);
  }
}
