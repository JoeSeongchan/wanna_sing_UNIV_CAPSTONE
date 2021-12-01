package com.example.detailgrouptest.home.recyclerview.adapter;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import com.example.detailgrouptest.db.entity.Party;
import com.example.detailgrouptest.home.recyclerview.viewholder.HomePartyViewHolder;
import com.example.detailgrouptest.home.recyclerview.viewholder.HomePartyViewHolder.OnPartyItemClickListener;

public class HomePartyAdapter extends ListAdapter<Party, HomePartyViewHolder> {

  private OnPartyItemClickListener onPartyItemClickListener;

  public HomePartyAdapter(
      @NonNull ItemCallback<Party> diffCallback,
      @NonNull OnPartyItemClickListener onPartyItemClickListener) {
    super(diffCallback);
    this.setHasStableIds(true);
    this.onPartyItemClickListener = onPartyItemClickListener;
  }

  @NonNull
  @Override
  public HomePartyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return HomePartyViewHolder.create(parent, onPartyItemClickListener);
  }

  @Override
  public int getItemViewType(int position) {
    return 0;
  }

  @Override
  public void onBindViewHolder(@NonNull HomePartyViewHolder holder, int position) {

    holder.bind(getItem(position));
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  public static class HomePartyDiff extends ItemCallback<Party> {

    @Override
    public boolean areItemsTheSame(@NonNull Party oldItem,
        @NonNull Party newItem) {
      return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Party oldItem,
        @NonNull Party newItem) {
      return oldItem.equals(newItem);
    }
  }
}
