package com.android.wannasing.feature.chat.showchatgroup.view;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.feature.chat.showchatgroup.view.BriefChatGroupViewHolder.onItemClickListener;


public class BriefChatGroupAdapter extends ListAdapter<ChatGroup, BriefChatGroupViewHolder> {

  private BriefChatGroupViewHolder.onItemClickListener onItemClickListener;

  public BriefChatGroupAdapter(
      @NonNull ItemCallback<ChatGroup> diffCallback,
      onItemClickListener onItemClickListener) {
    super(diffCallback);
    this.onItemClickListener = onItemClickListener;
    this.setHasStableIds(true);
  }

  @NonNull
  @Override
  public BriefChatGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    BriefChatGroupViewHolder viewHolder = BriefChatGroupViewHolder.create(parent);
    viewHolder.setOnItemClickListener(onItemClickListener);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull BriefChatGroupViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  public ChatGroup getItem(int pos) {
    return super.getItem(pos);
  }

  @Override
  public long getItemId(int position) {
    return getCurrentList().get(position).hashCode();
  }

  public static class BriefChatGroupDiff extends ItemCallback<ChatGroup> {

    @Override
    public boolean areItemsTheSame(@NonNull ChatGroup oldItem,
        @NonNull ChatGroup newItem) {
      return oldItem.getPrimaryKey().compareTo(newItem.getPrimaryKey()) == 0 &&
          oldItem.getLastMsg().compareTo(newItem.getLastMsg()) == 0;
    }

    @Override
    public boolean areContentsTheSame(@NonNull ChatGroup oldItem,
        @NonNull ChatGroup newItem) {
      return oldItem.getPrimaryKey().compareTo(newItem.getPrimaryKey()) == 0 &&
          oldItem.getLastMsg().compareTo(newItem.getLastMsg()) == 0;
    }
  }
}
