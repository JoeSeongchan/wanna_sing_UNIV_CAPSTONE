package com.android.wannasing.showpartyinfo.recyclerview.viewholder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.databinding.ItemMemberShowPartyInfoBinding;
import java.util.EventListener;

public class MemberViewHolder extends RecyclerView.ViewHolder {

  private String memberId;

  private ItemMemberShowPartyInfoBinding binding;

  public MemberViewHolder(ItemMemberShowPartyInfoBinding binding,
      OnMemberItemClickListener onMemberItemClickListener) {
    super(binding.getRoot());
    this.binding = binding;
    binding.partyMemberTvId
        .setOnClickListener(v -> onMemberItemClickListener.onMemberItemClick(memberId));
  }

  @NonNull
  public static MemberViewHolder create(@NonNull ViewGroup parent,
      OnMemberItemClickListener onMemberItemClickListener) {
    return new MemberViewHolder(
        ItemMemberShowPartyInfoBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false), onMemberItemClickListener
    );
  }

  public void bind(@NonNull String memberId, MemberType memberType) {
    Context context = binding.getRoot().getContext();
    this.memberId = memberId;
    switch (memberType) {
      case HOST:
        binding.partyMemberTvId
            .setText(context.getString(R.string.showPartyInfo_btn_hostMember, memberId));
        binding.partyMemberTvId
            .setBackground(AppCompatResources.getDrawable(context, R.drawable.btn_host_member));
        break;
      case MEMBER:
        binding.partyMemberTvId
            .setText(context.getString(R.string.showPartyInfo_btn_member, memberId));
        binding.partyMemberTvId
            .setBackground(AppCompatResources.getDrawable(context, R.drawable.btn_member));
        break;
    }
  }

  public enum MemberType {
    HOST, MEMBER;

    public static MemberType getMemberType(int index) {
      return MemberType.values()[index];
    }

    public int getIndex() {
      return ordinal();
    }
  }

  public interface OnMemberItemClickListener extends EventListener {

    void onMemberItemClick(String memberId);
  }
}
