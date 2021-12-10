package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.databinding.ItemChatGroupBinding;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import java.text.SimpleDateFormat;
import java.util.EventListener;

public class ShowChatGroupViewHolder extends RecyclerView.ViewHolder {

  private final ItemChatGroupBinding binding;
  private ChatGroup chatGroup;

  public ShowChatGroupViewHolder(ItemChatGroupBinding binding,
      OnChatGroupItemClickListener onChatGroupItemClickListener) {
    super(binding.getRoot());
    this.binding = binding;
    binding.chatGroupItemClContainer
        .setOnClickListener(v -> onChatGroupItemClickListener.onPartyItemClick(chatGroup));
  }

  @NonNull
  public static ShowChatGroupViewHolder create(@NonNull ViewGroup parent,
      OnChatGroupItemClickListener onChatGroupItemClickListener) {
    return new ShowChatGroupViewHolder(
        ItemChatGroupBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false), onChatGroupItemClickListener
    );
  }

  public void bind(@NonNull ChatGroup chatGroup) {
    this.chatGroup = chatGroup;
    binding.chatGroupItemTvGroupTitle.setText(chatGroup.getName());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    binding.chatGroupItemTvMeetingDate.setText(simpleDateFormat.format(chatGroup.getMeetingDate()));
  }

  public interface OnChatGroupItemClickListener extends EventListener {

    void onPartyItemClick(ChatGroup chatGroup);
  }
}
