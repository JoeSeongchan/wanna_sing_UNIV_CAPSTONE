package com.example.detailgrouptest.showpartyinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.detailgrouptest.R;
import com.example.detailgrouptest.databinding.ActivityShowPartyInfoBinding;
import com.example.detailgrouptest.db.entity.Party;
import com.example.detailgrouptest.db.entity.Party.AgeDetail;
import com.example.detailgrouptest.db.entity.Party.Gender;
import com.example.detailgrouptest.db.entity.Party.Genre;
import com.example.detailgrouptest.db.entity.Party.MyTime;
import com.example.detailgrouptest.showpartyinfo.recyclerview.adapter.MemberAdapter;
import com.example.detailgrouptest.showpartyinfo.recyclerview.adapter.MemberAdapter.MemberDiff;
import com.example.detailgrouptest.showpartyinfo.recyclerview.viewholder.MemberViewHolder.OnMemberItemClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShowPartyInfoActivity extends AppCompatActivity implements OnMemberItemClickListener {

  public static final String TAG = "ShowPartyInfoActivity_R";
  private ActivityShowPartyInfoBinding binding;
  private Party party;
  private FirebaseFirestore fireDb;
  private MemberViewModel memberViewModel;

  private RecyclerView rv;
  private MemberAdapter adapter;

  public void getPartyInfoFromFormerActivity() {
    party = Optional
        .ofNullable(getIntent()).map(Intent::getExtras)
        .map(bundle -> (Party) bundle.getSerializable("PARTY_INFO"))
        .orElseGet(() -> {
          List<Genre> genreList = new ArrayList<>();
          genreList.add(Genre.FREE);
          return new Party(
              "dummy_host_id",
              "dummy_party_name",
              genreList,
              Gender.FREE,
              0,
              new ArrayList<>(),
              "dummy_karaoke_id",
              "dummy_karaoke_name",
              new Date(),
              3,
              5,
              new MyTime(LocalTime.now()),
              new MyTime(LocalTime.now())
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

  private void initRecyclerView() {
    rv = binding.showPartyInfoRvMemberList;
    adapter = new MemberAdapter(new MemberDiff(), this, party.hostID);
    rv.setLayoutManager(new LinearLayoutManager(this));
    rv.setAdapter(adapter);
  }

  private void setMemberViewModel() {
    MemberViewModelFactory factory = new MemberViewModelFactory(FirebaseFirestore.getInstance(),
        party.hostID, party.partyName);
    memberViewModel = new ViewModelProvider(this, factory).get(MemberViewModel.class);
  }

  private void setDb() {
    fireDb = FirebaseFirestore.getInstance();
  }

  private void setUi() {
    showPartyInfoThroughUi();
    initRecyclerView();
    setMemberViewModel();
    showMemberInfoThroughUi();
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
        party.meetingStartTime.hour,
        party.meetingStartTime.minutes));
  }

  private void showMemberInfoThroughUi() {
    // view model 에서 멤버 id 리스트 가져옴.
    memberViewModel.getMemberIdList().observe(this, memberIdList ->
        adapter.submitList(memberIdList));
  }

  @Override
  public void onMemberItemClick(String memberId) {
    Toast.makeText(this, String.format("member id : %1$s", memberId), Toast.LENGTH_SHORT).show();
  }
}
