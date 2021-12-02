package com.android.wannasing.home.fragment.partygroup.recyclerview.adapter;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import com.android.wannasing.db.entity.Party;
import com.android.wannasing.home.fragment.partygroup.recyclerview.viewholder.PartyInHomeViewHolder;
import com.android.wannasing.home.fragment.partygroup.recyclerview.viewholder.PartyInHomeViewHolder.OnPartyItemClickListener;

public class PartyInHomeAdapter extends ListAdapter<Party, PartyInHomeViewHolder> {

  private OnPartyItemClickListener onPartyItemClickListener;

  public PartyInHomeAdapter(
      @NonNull ItemCallback<Party> diffCallback,
      @NonNull OnPartyItemClickListener onPartyItemClickListener) {
    super(diffCallback);
    this.setHasStableIds(true);
    this.onPartyItemClickListener = onPartyItemClickListener;
  }

  @NonNull
  @Override
  public PartyInHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return PartyInHomeViewHolder.create(parent, onPartyItemClickListener);
  }

  @Override
  public int getItemViewType(int position) {
    return 0;
  }

  @Override
  public void onBindViewHolder(@NonNull PartyInHomeViewHolder holder, int position) {

    holder.bind(getItem(position));
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  public static class PartyDiff extends ItemCallback<Party> {

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
