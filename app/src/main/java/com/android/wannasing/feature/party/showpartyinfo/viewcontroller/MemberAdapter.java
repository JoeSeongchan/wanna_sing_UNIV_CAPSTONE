package com.android.wannasing.feature.party.showpartyinfo.viewcontroller;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import com.android.wannasing.feature.party.showpartyinfo.viewcontroller.MemberViewHolder.MemberType;
import com.android.wannasing.feature.party.showpartyinfo.viewcontroller.MemberViewHolder.OnMemberItemClickListener;

public class MemberAdapter extends ListAdapter<String, MemberViewHolder> {

  String hostId;
  private OnMemberItemClickListener onMemberItemClickListener;

  public MemberAdapter(
      @NonNull ItemCallback<String> diffCallback,
      @NonNull OnMemberItemClickListener onMemberItemClickListener,
      @NonNull String hostId) {
    super(diffCallback);
    this.setHasStableIds(true);
    this.onMemberItemClickListener = onMemberItemClickListener;
    this.hostId = hostId;
  }

  @NonNull
  @Override
  public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return MemberViewHolder.create(parent, onMemberItemClickListener);
  }

  @Override
  public int getItemViewType(int position) {
    if (getItem(position).equals(hostId)) {
      return MemberType.HOST.getIndex();
    } else {
      return MemberType.MEMBER.getIndex();
    }
  }

  @Override
  public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
    holder.bind(getItem(position), MemberType.getMemberType(getItemViewType(position)));
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  public static class MemberDiff extends ItemCallback<String> {

    @Override
    public boolean areItemsTheSame(@NonNull String oldItem,
        @NonNull String newItem) {
      return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull String oldItem,
        @NonNull String newItem) {
      return oldItem.equals(newItem);
    }
  }
}
