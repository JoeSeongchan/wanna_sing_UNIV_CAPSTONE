package com.android.wannasing.feature.map.viewcontroller;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.android.wannasing.R;
import com.android.wannasing.feature.party.createparty.viewcontroller.CreatePartyActivity;

public class ShowKaraokeInfoFragment extends DialogFragment {


  private static String karaokeId = "karaokeId";
  private static String title_s = "TITLE";
  private static String phone_s = "PHONE";
  private static String address1_s = "ADDRESS1";
  private static String address2_s = "ADDRESS2";


  public static ShowKaraokeInfoFragment newInstance(String KaraokeId, String Title_D,
      String Phonenum_D,
      String Address1_D,
      String Address2_D) {
    //Log.d("WANTSLEEP","????\n"+"Place.name: "+Title_D+" Phone num: "+Phonenum_D+" Address1: " +Address1_D+" Address2: "+Address2_D+"\n");

    karaokeId = KaraokeId;
    title_s = Title_D;
    phone_s = Phonenum_D;
    address1_s = Address1_D;
    address2_s = Address2_D;

    Bundle bundle = new Bundle();
    bundle.putString(karaokeId, KaraokeId);
    bundle.putString(title_s, Title_D);
    bundle.putString(phone_s, Phonenum_D);
    bundle.putString(address1_s, Address1_D);
    bundle.putString(address2_s, Address2_D);
    //Log.d("WANTSLEEP","BUNDLE\n"+"Place.name: "+title_s+" Phone num: "+phone_s+" Address1: " +address1_s+" Address2: "+address2_s+"\n");

    ShowKaraokeInfoFragment fragment = new ShowKaraokeInfoFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    View view = getActivity().getLayoutInflater()
        .inflate(R.layout.fragment_show_karaoke_info, null);
    ((TextView) view.findViewById(R.id.showKaraokeInfo_tv_title)).setText(title_s);
    ((TextView) view.findViewById(R.id.showKaraokeInfo_tv_phone)).setText(phone_s);
    ((TextView) view.findViewById(R.id.showKaraokeInfo_tv_address1)).setText(address1_s);
    ((TextView) view.findViewById(R.id.showKaraokeInfo_tv_address2)).setText(address2_s);

    builder.setView(view);

    view.findViewById(R.id.showKaraokeInfo_btn_createParty)
        .setOnClickListener(v -> {
          Intent intent = new Intent(getActivity(), CreatePartyActivity.class);
          intent
              .putExtra(CreatePartyActivity.FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_ID_TAG, karaokeId);
          intent
              .putExtra(CreatePartyActivity.FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_NAME_TAG, title_s);
          startActivity(intent);
        });

    view.findViewById(R.id.showKaraokeInfo_btn_showPartyList)
        .setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), 해당노래방에서열리는모임만보여주는클래스.class);
//            intent.putExtra("KaraokeID", karaokeId);
//            startActivity(intent);
        });

    return builder.create();
  }

  private void setOnClickBtnCreateParty(View view) {

  }
}