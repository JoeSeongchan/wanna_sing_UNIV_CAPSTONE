package com.android.wannasing.feature.party.showpartyinfo.viewcontroller;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.databinding.ItemMemberBinding;
import java.util.EventListener;

public class MemberViewHolder extends RecyclerView.ViewHolder {

  private String memberId;

  private ItemMemberBinding binding;

  public MemberViewHolder(ItemMemberBinding binding,
      OnMemberItemClickListener onMemberItemClickListener) {
    super(binding.getRoot());
    this.binding = binding;
    binding.memberItemTvId
        .setOnClickListener(v -> onMemberItemClickListener.onMemberItemClick(memberId));
  }

  @NonNull
  public static MemberViewHolder create(@NonNull ViewGroup parent,
      OnMemberItemClickListener onMemberItemClickListener) {
    return new MemberViewHolder(
        ItemMemberBinding.inflate(
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
        binding.memberItemTvId
            .setText(context.getString(R.string.showPartyInfo_btn_hostMember, memberId));
        binding.memberItemTvId
            .setBackground(AppCompatResources
                .getDrawable(context, R.drawable.show_party_info_host_background));
        break;
      case MEMBER:
        binding.memberItemTvId
            .setText(context.getString(R.string.showPartyInfo_btn_member, memberId));
        binding.memberItemTvId
            .setBackground(AppCompatResources.getDrawable(context, R.drawable.login_border));
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
