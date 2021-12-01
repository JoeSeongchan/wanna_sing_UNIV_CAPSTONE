package com.android.wannasing.home.recyclerview.viewholder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.databinding.ItemPartyMainBinding;
import com.android.wannasing.db.entity.Party;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.EventListener;

public class HomePartyViewHolder extends RecyclerView.ViewHolder {

  private Party party;

  private ItemPartyMainBinding binding;

  public HomePartyViewHolder(ItemPartyMainBinding binding,
      OnPartyItemClickListener onPartyItemClickListener) {
    super(binding.getRoot());
    this.binding = binding;
    binding.homePartyItemRlContainer
        .setOnClickListener(v -> onPartyItemClickListener.onPartyItemClick(party));
  }

  @NonNull
  public static HomePartyViewHolder create(@NonNull ViewGroup parent,
      OnPartyItemClickListener onPartyItemClickListener) {
    return new HomePartyViewHolder(
        ItemPartyMainBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false), onPartyItemClickListener
    );
  }

  public void bind(@NonNull Party party) {
    Context context = binding.getRoot().getContext();
    this.party = party;
    binding.homePartyItemTvPartyName.setText(party.partyName);
    binding.homePartyItemTvKaraokeName.setText(party.karaokeName);
    binding.homePartyItemTvMemberInfo.setText(context
        .getString(R.string.homePartyItem_tv_memberInfo,
            party.curMemberNum,
            party.maxMemberNum));
    Calendar calendarForDate = Calendar.getInstance();
    calendarForDate.setTime(party.meetingDate);
    binding.homePartyItemTvStartDateTime.setText(context
        .getString(R.string.homePartyItem_tv_startDateTime,
            calendarForDate.get(Calendar.MONTH),
            calendarForDate.get(Calendar.DAY_OF_MONTH),
            party.meetingStartTime.hour,
            party.meetingStartTime.minutes));
    LocalTime now = LocalTime.now();
    long duration = party.meetingStartTime.subtract(now);
    binding.homePartyItemTvTimeWarning.setText(context
        .getString(R.string.homePartyItem_tv_timeWarning,
            duration
        ));
  }

  public interface OnPartyItemClickListener extends EventListener {

    void onPartyItemClick(Party party);
  }
}
