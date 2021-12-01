package com.example.detailgrouptest.showpartyinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.detailgrouptest.R;
import com.example.detailgrouptest.databinding.ActivityShowPartyInfoBinding;
import com.example.detailgrouptest.db.entity.Joins;
import com.example.detailgrouptest.db.entity.Party;
import com.example.detailgrouptest.db.entity.Party.AgeDetail;
import com.example.detailgrouptest.db.entity.Party.Gender;
import com.example.detailgrouptest.db.entity.Party.Genre;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShowPartyInfoActivity extends AppCompatActivity {

  public static final String TAG = "ShowPartyInfoActivity_R";
  private ActivityShowPartyInfoBinding binding;
  private Party party;
  private FirebaseFirestore fireDb;
  private List<String> memberList;

  public void getPartyInfoFromFormerActivity() {
    party = Optional
        .ofNullable(getIntent()).map(Intent::getExtras)
        .map(bundle -> (Party) bundle.getSerializable("PARTY_INFO"))
        .orElseGet(() -> {
          List<Genre> genreList = new ArrayList<>();
          genreList.add(Genre.FREE);
          return new Party(
              "dummy_host_id",
              "dummy_party_id",
              genreList,
              Gender.FREE,
              0,
              new ArrayList<>(),
              "dummy_karaoke_id",
              "dummy_karaoke_name",
              new Date(),
              3,
              5,
              LocalTime.now(),
              LocalTime.now()
          );
        });
  }


  public void onCreate(Bundle savedInstanceState) {
    binding = ActivityShowPartyInfoBinding
        .inflate(getLayoutInflater());
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());
    getPartyInfoFromFormerActivity();
    setDb();
    setUi();
  }

  private void setDb() {
    fireDb = FirebaseFirestore.getInstance();
  }

  private void setUi() {
    showPartyInfoThroughUi();
    getAllMemberInfoFromFireDb();
  }

  private void showPartyInfoThroughUi() {
    Gender gender = party.gender;
    int age = party.age;
    List<AgeDetail> ageDetailList = party.ageDetailList;
    List<Genre> genreList = party.genreList;
    binding.showPartyInfoTvGenderCondition
        .setText(getString(R.string.showPartyInfo_tv_genderCondition, gender.getValue()));
    String ageConditions;
    binding.showPartyInfoTvTitle.setText(party.partyName);
    if (ageDetailList.isEmpty()) {
      ageConditions = "#나이_상관없음";
    } else {
      ageConditions = ageDetailList.stream().map(ageDetail -> age + "_" + ageDetail.getValue())
          .map(str -> getString(R.string.showPartyInfo_tv_ageCondition, str))
          .collect(Collectors.joining(", "));
    }
    binding.showPartyInfoTvAgeCondition.setText(ageConditions);
    String genreConditions = genreList.stream().map(Genre::getValue)
        .map(str -> getString(R.string.showPartyInfo_tv_genreCondition, str))
        .collect(Collectors.joining(", "));
    binding.showPartyInfoTvGenreCondition.setText(genreConditions);

    binding.showPartyInfoTvMemberNum
        .setText(
            getString(R.string.showPartyInfo_tv_memberNum,
                party.curMemberNum,
                party.maxMemberNum));
    binding.showPartyInfoTvKaraokeName.setText(party.karaokeId);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(party.meetingDate);
    binding.showPartyInfoTvPartyDate.setText(getString(R.string.showPartyInfo_tv_partyDate,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)));
    binding.showPartyInfoTvPartyTime.setText(getString(R.string.showPartyInfo_tv_partyTime,
        party.meetingStartTime.getHour(),
        party.meetingStartTime.getMinute()));
  }

  private void getAllMemberInfoFromFireDb() {
    fireDb.collection("joins_list").whereEqualTo("memberId", party.hostID)
        .get().addOnSuccessListener(queryDocumentSnapshots -> {
      if (queryDocumentSnapshots.isEmpty()) {
        Log.d(TAG, "query empty.");
        memberList = new ArrayList<>();
        memberList.add("dummy_member_1");
        memberList.add("dummy_member_2");
        showMemberInfoThroughUi();
      } else {
        memberList =
            queryDocumentSnapshots.toObjects(Joins.class)
                .stream().map(joins -> joins.memberId).collect(Collectors.toList());
        memberList.remove(party.hostID);
        showMemberInfoThroughUi();
      }
    }).addOnFailureListener(err -> {
    });
  }

  private void showMemberInfoThroughUi() {
    binding.showPartyInfoBtnHostMember
        .setText(getString(R.string.showPartyInfo_btn_hostMember, party.hostID));

    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    LinearLayout memberListLayout = binding.showPartyInfoLlMember;
    memberList.forEach(member -> {
      View template = inflater.inflate(R.layout.view_btn_member, null);
      AppCompatButton btnMember = template.findViewById(R.id.showPartyInfo_btn_member);
      btnMember.setText(getString(R.string.showPartyInfo_btn_member, member));
      ViewGroup parentViewGroup = (ViewGroup) btnMember.getParent();
      if (null != parentViewGroup) {
        parentViewGroup.removeView(btnMember);
      }
      memberListLayout.addView(btnMember);
    });
  }


  private void isInGroup() {
    fireDb.collection("joins_list").whereEqualTo("memberId", party.hostID)
        .get().addOnSuccessListener(queryDocumentSnapshots -> {
      if (queryDocumentSnapshots.isEmpty()) {
        Log.d(TAG, "query empty.");
        memberList = new ArrayList<>();
        memberList.add("dummy_member_1");
        memberList.add("dummy_member_2");
        showMemberInfoThroughUi();
      } else {
        memberList =
            queryDocumentSnapshots.toObjects(Joins.class)
                .stream().map(joins -> joins.memberId).collect(Collectors.toList());
        memberList.remove(party.hostID);
        showMemberInfoThroughUi();
      }
    }).addOnFailureListener(err -> {
    });

  }
}
