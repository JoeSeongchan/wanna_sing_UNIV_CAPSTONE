package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.ShowChatGroupViewHolder.OnChatGroupItemClickListener;

public class ShowChatGroupAdapter extends ListAdapter<ChatGroup, ShowChatGroupViewHolder> {

  private final OnChatGroupItemClickListener onChatGroupItemClickListener;

  public ShowChatGroupAdapter(
      @NonNull ItemCallback<ChatGroup> diffCallback,
      @NonNull OnChatGroupItemClickListener onChatGroupItemClickListener) {
    super(diffCallback);
    this.setHasStableIds(true);
    this.onChatGroupItemClickListener = onChatGroupItemClickListener;
  }

  @NonNull
  @Override
  public ShowChatGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return ShowChatGroupViewHolder.create(parent, onChatGroupItemClickListener);
  }

  @Override
  public int getItemViewType(int position) {
    return 0;
  }

  @Override
  public void onBindViewHolder(@NonNull ShowChatGroupViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  public static class ChatGroupDiff extends ItemCallback<ChatGroup> {

    @Override
    public boolean areItemsTheSame(@NonNull ChatGroup oldItem,
        @NonNull ChatGroup newItem) {
      return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull ChatGroup oldItem,
        @NonNull ChatGroup newItem) {
      return oldItem.equals(newItem);
    }
  }
}
