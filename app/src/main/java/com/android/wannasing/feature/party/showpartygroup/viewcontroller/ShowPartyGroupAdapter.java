package com.android.wannasing.feature.party.showpartygroup.viewcontroller;


import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.feature.party.showpartygroup.viewcontroller.ShowPartyGroupViewHolder.OnPartyItemClickListener;

public class ShowPartyGroupAdapter extends ListAdapter<Party, ShowPartyGroupViewHolder> {

  private OnPartyItemClickListener onPartyItemClickListener;

  public ShowPartyGroupAdapter(
      @NonNull ItemCallback<Party> diffCallback,
      @NonNull OnPartyItemClickListener onPartyItemClickListener) {
    super(diffCallback);
    this.setHasStableIds(true);
    this.onPartyItemClickListener = onPartyItemClickListener;
  }

  @NonNull
  @Override
  public ShowPartyGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return ShowPartyGroupViewHolder.create(parent, onPartyItemClickListener);
  }

  @Override
  public int getItemViewType(int position) {
    return 0;
  }

  @Override
  public void onBindViewHolder(@NonNull ShowPartyGroupViewHolder holder, int position) {

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
