package com.android.wannasing.feature.party.showpartygroup.viewcontroller;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.databinding.ItemPartyBinding;
import com.android.wannasing.feature.party.common.model.Party;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.EventListener;

public class ShowPartyGroupViewHolder extends RecyclerView.ViewHolder {

  private Party party;

  private ItemPartyBinding binding;

  public ShowPartyGroupViewHolder(ItemPartyBinding binding,
      OnPartyItemClickListener onPartyItemClickListener) {
    super(binding.getRoot());
    this.binding = binding;
    binding.partyItemRlContainer
        .setOnClickListener(v -> onPartyItemClickListener.onPartyItemClick(party));
  }

  @NonNull
  public static ShowPartyGroupViewHolder create(@NonNull ViewGroup parent,
      OnPartyItemClickListener onPartyItemClickListener) {
    return new ShowPartyGroupViewHolder(
        ItemPartyBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false), onPartyItemClickListener
    );
  }

  public void bind(@NonNull Party party) {
    Context context = binding.getRoot().getContext();
    this.party = party;
    binding.partyItemTvPartyName.setText(party.partyName);
    binding.partyItemTvKaraokeName.setText(party.karaokeName);
    binding.partyItemTvMemberInfo.setText(context
        .getString(R.string.partyItem_tv_memberInfo,
            party.curMemberNum,
            party.maxMemberNum));
    Calendar calendarForDate = Calendar.getInstance();
    calendarForDate.setTime(party.meetingDate);
    binding.partyItemTvStartDateTime.setText(context
        .getString(R.string.partyItem_tv_startDateTime,
            calendarForDate.get(Calendar.MONTH),
            calendarForDate.get(Calendar.DAY_OF_MONTH),
            party.meetingStartTime.hour,
            party.meetingStartTime.minutes));
    LocalTime now = LocalTime.now();
    long duration = party.meetingStartTime.subtract(now);
//    binding.partyItemTvTimeWarning.setText(context
//        .getString(R.string.partyItem_tv_timeWarning,
//            duration
//        ));
  }

  public interface OnPartyItemClickListener extends EventListener {

    void onPartyItemClick(Party party);
  }
}
