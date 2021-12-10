package com.android.wannasing.feature.party.createparty.viewcontroller;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;
import com.android.wannasing.common.model.Joins;
import com.android.wannasing.databinding.ActivityCreatePartyBinding;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.feature.party.common.model.Party.AgeDetail;
import com.android.wannasing.feature.party.common.model.Party.Gender;
import com.android.wannasing.feature.party.common.model.Party.Genre;
import com.android.wannasing.feature.party.common.model.Party.MyTime;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;


public class CreatePartyActivity extends AppCompatActivity {

  public static final String FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_ID_TAG
      = "FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_ID_TAG";
  public static final String FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_NAME_TAG
      = "FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_NAME_TAG";
  public static final String FROM_SHOW_KARAOKE_INFO_FRAG_HOST_ID_TAG
      = "FROM_SHOW_KARAOKE_INFO_FRAG_HOST_ID_TAG";
  public static final String PARTY_COLLECTION_PATH = "party_list";
  public static final String JOINS_COLLECTION_PATH = "joins_list";

  private final List<Genre> genreList = new ArrayList<>();
  private final List<AgeDetail> ageDetail = new ArrayList<>();
  private ActivityCreatePartyBinding binding;
  private String hostId = "05uJa1ZLdMWJEetdVxIBMVoZmVG3";
  private FirebaseFirestore fireDb;
  private String groupName = null;
  private Gender gender = null;
  private int age = 0;
  private Date meetingDate = null;
  private MyTime startTime = null;
  private MyTime endTime = null;
  private int memMax = 1;
  private String karaokeId = null;
  private String karaokeName = null;

  public void onCreate(Bundle savedInstanceState) {
    binding = ActivityCreatePartyBinding
        .inflate(getLayoutInflater());
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());
    getKaraokeInfoFromShowKaraokeInfoFragment();
    getUserIdFromShowKaraokeInfoFragment();
    setDb();
    setUi();
  }

  private void getKaraokeInfoFromShowKaraokeInfoFragment() {
    this.karaokeId = Optional
        .ofNullable(getIntent().getStringExtra(FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_ID_TAG))
        .orElse("dummy_karaoke_id");
    this.karaokeName = Optional
        .ofNullable(getIntent().getStringExtra(FROM_SHOW_KARAOKE_INFO_FRAG_KARAOKE_NAME_TAG))
        .orElse("dummy_karaoke_name");
  }

  private void getUserIdFromShowKaraokeInfoFragment() {
    this.hostId = Optional
        .ofNullable(getIntent().getStringExtra(FROM_SHOW_KARAOKE_INFO_FRAG_HOST_ID_TAG))
        .orElse("05uJa1ZLdMWJEetdVxIBMVoZmVG3");
  }

  private void setDb() {
    fireDb = FirebaseFirestore.getInstance();
  }

  private void setUi() {
    setPartyTitleLayout();
    setGenreBtnClickListener();
    setGenderBtnClickListener();
    setAgeBtnClickListener();
    setCalendarClickListener();
    setTimeClickListener();
    setMemberMaxNumClickListener();
    setCreatePartyBtnClickListener();
  }

  private void setPartyTitleLayout() {
    // party 제목 입력창 최대 글자 수 설정.
    binding.createPartyTilGroupTitle.setCounterEnabled(true);
    binding.createPartyTilGroupTitle.setCounterMaxLength(10);
    binding.createPartyEtGroupTitle.addTextChangedListener(new TextWatcher() {
      final TextInputLayout layout = binding.createPartyTilGroupTitle;

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
          layout.setError("모임명을 입력해주세요.");
        } else {
          groupName = s.toString();
          layout.setError(null); //에러 메시지를 지워주는 기능
        }
      }
    });
  }

  // 장르 버튼 클릭한 경우,
  private void setGenreBtnClickListener() {
    binding.createPartyBtnGenreFree.setOnClickListener(v -> {
      binding.createPartyBtnGenreBalad.setSelected(false);
      binding.createPartyBtnGenreHiphop.setSelected(false);
      binding.createPartyBtnGenreJpop.setSelected(false);
      binding.createPartyBtnGenreOldSong.setSelected(false);
      binding.createPartyBtnGenreNormalSong.setSelected(false);
      binding.createPartyBtnGenreOther.setSelected(false);
      binding.createPartyBtnGenrePop.setSelected(false);
      binding.createPartyBtnGenreTop.setSelected(false);
      binding.createPartyBtnGenreFree.setSelected(true);
      genreList.clear();
      genreList.add(Genre.FREE);
    });

    binding.createPartyBtnGenreBalad.setOnClickListener(v -> {
      // 기존에 사용자가 버튼을 이미 클릭한 경우,
      if (binding.createPartyBtnGenreBalad.isSelected()) {
        // 해당 버튼 select 해제.
        binding.createPartyBtnGenreBalad.setSelected(false);
        // balad genre 제거.
        genreList.remove(Genre.BALAD);
      }
      // 사용자가 처음 버튼을 클릭한 경우
      else {
        binding.createPartyBtnGenreFree.setSelected(false);
        // 해당 버튼 select 설정.
        binding.createPartyBtnGenreBalad.setSelected(true);
        // free genre 해제.
        genreList.remove(Genre.FREE);
        // balad genre 추가.
        genreList.add(Genre.BALAD);
      }
    });
    binding.createPartyBtnGenreHiphop.setOnClickListener(v -> {
      if (binding.createPartyBtnGenreHiphop.isSelected()) {
        binding.createPartyBtnGenreHiphop.setSelected(false);
        genreList.remove(Genre.HIPHOP);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenreHiphop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.HIPHOP);
      }
    });
    binding.createPartyBtnGenreJpop.setOnClickListener(v -> {
      if (binding.createPartyBtnGenreJpop.isSelected()) {
        binding.createPartyBtnGenreJpop.setSelected(false);
        genreList.remove(Genre.JPOP);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenreJpop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.JPOP);
      }
    });
    binding.createPartyBtnGenreOldSong.setOnClickListener(v -> {

      if (binding.createPartyBtnGenreOldSong.isSelected()) {
        binding.createPartyBtnGenreOldSong.setSelected(false);
        genreList.remove(Genre.OLD_SONG);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenreOldSong.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.OLD_SONG);
      }
    });
    binding.createPartyBtnGenreNormalSong.setOnClickListener(v -> {
      if (binding.createPartyBtnGenreNormalSong.isSelected()) {
        binding.createPartyBtnGenreNormalSong.setSelected(false);
        genreList.remove(Genre.NORMAL_SONG);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenreNormalSong.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.NORMAL_SONG);
      }
    });
    binding.createPartyBtnGenreOther.setOnClickListener(v -> {
      if (binding.createPartyBtnGenreOther.isSelected()) {
        binding.createPartyBtnGenreOther.setSelected(false);
        genreList.remove(Genre.OTHER);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenreOther.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.OTHER);
      }
    });
    binding.createPartyBtnGenrePop.setOnClickListener(v -> {
      if (binding.createPartyBtnGenrePop.isSelected()) {
        binding.createPartyBtnGenrePop.setSelected(false);
        genreList.remove(Genre.POP);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenrePop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.POP);
      }
    });
    binding.createPartyBtnGenreTop.setOnClickListener(v -> {
      if (binding.createPartyBtnGenreTop.isSelected()) {
        binding.createPartyBtnGenreTop.setSelected(false);
        genreList.remove(Genre.TOP);
      } else {
        binding.createPartyBtnGenreFree.setSelected(false);
        binding.createPartyBtnGenreTop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.TOP);
      }
    });
  }

  // '성별' 버튼 클릭한 경우,
  private void setGenderBtnClickListener() {
    binding.createPartyBtnGenderFree.setOnClickListener(v -> {
      v.setSelected(true);
      binding.createPartyBtnGenderMale.setSelected(false);
      binding.createPartyBtnGenderFemale.setSelected(false);
      gender = Gender.FREE;
    });
    binding.createPartyBtnGenderMale.setOnClickListener(v -> {
      binding.createPartyBtnGenderFree.setSelected(false);
      v.setSelected(true);
      binding.createPartyBtnGenderFemale.setSelected(false);
      gender = Gender.MALE;
    });
    binding.createPartyBtnGenderFemale.setOnClickListener(v -> {
      binding.createPartyBtnGenderFree.setSelected(false);
      binding.createPartyBtnGenderMale.setSelected(false);
      v.setSelected(true);
      gender = Gender.FEMALE;
    });
  }

  // '나이' 버튼 클릭한 경우,
  private void setAgeBtnClickListener() {
    binding.createPartyTvAge.setText("나이 자유");
    // 왼쪽 화살표 버튼 클릭한 경우,
    binding.createPartyBtnAgeLeft.setOnClickListener(v -> {
      if (age > 0) {
        age -= 10;
        if (age > 0) {
          binding.createPartyTvAge
              .setText(getString(R.string.createParty_tv_age, age));
        } else if (age == 0) {
          binding.createPartyTvAge.setText("나이 자유");
          binding.createPartyBtnAgeEarly.setSelected(false);
          binding.createPartyBtnAgeMid.setSelected(false);
          binding.createPartyBtnAgeLate.setSelected(false);
          ageDetail.clear();
        }
      }
    });
    // 오른쪽 화살표 버튼 클릭한 경우,
    binding.createPartyBtnAgeRight.setOnClickListener(v -> {
      if (age < 90) {
        age += 10;
        binding.createPartyTvAge.setText(getString(R.string.createParty_tv_age, age));
      }
    });
    // '초반 버튼' 클릭한 경우,
    binding.createPartyBtnAgeEarly.setOnClickListener(v -> {
      if (age == 0) {
        return;
      }
      if (v.isSelected()) {
        v.setSelected(false);
        ageDetail.remove(AgeDetail.EARLY);
      } else {
        v.setSelected(true);
        ageDetail.add(AgeDetail.EARLY);
      }
    });
    // '중반 버튼' 클릭한 경우,
    binding.createPartyBtnAgeMid.setOnClickListener(v -> {
      if (age == 0) {
        return;
      }
      if (v.isSelected()) {
        v.setSelected(false);
        ageDetail.remove(AgeDetail.MID);
      } else {
        v.setSelected(true);
        ageDetail.add(AgeDetail.MID);
      }
    });
    // '후반 버튼' 클릭한 경우,
    binding.createPartyBtnAgeLate.setOnClickListener(v -> {
      if (age == 0) {
        return;
      }
      if (v.isSelected()) {
        v.setSelected(false);
        ageDetail.remove(AgeDetail.LATE);
      } else {
        v.setSelected(true);
        ageDetail.add(AgeDetail.LATE);
      }
    });
  }

  // 캘린더 picker 버튼 클릭한 경우,
  private void setCalendarClickListener() {
    binding.createPartyBtnSelectDate.setOnClickListener(v -> {
      final Calendar c = Calendar.getInstance();
      int mYear = c.get(Calendar.YEAR);
      int mMonth = c.get(Calendar.MONTH);
      int mDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this,
          (view, year, month, dayOfMonth) -> {
            binding.createPartyBtnSelectDate
                .setText(year + " / " + (month + 1) + " / " + dayOfMonth);
            meetingDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
          }, mYear, mMonth, mDay);
      datePickerDialog.show();
    });
  }

  // 시간 picker 버튼 클릭한 경우,
  private void setTimeClickListener() {
    binding.createPartyBtnSelectStartTime.setOnClickListener(v -> {
      final Calendar c = Calendar.getInstance();
      int mHour = c.get(Calendar.HOUR);
      int mMin = c.get(Calendar.MINUTE);
      TimePickerDialog timePickerDialog =
          new TimePickerDialog(this,
              (view, hourOfDay, minute) -> {
                binding.createPartyBtnSelectStartTime
                    .setText(String.format("%02d:%02d", hourOfDay, minute));
                startTime = new MyTime(hourOfDay, minute);
              }, mHour, mMin, false);
      timePickerDialog.show();
    });
    binding.createPartyBtnSelectEndTime.setOnClickListener(v -> {
      final Calendar c = Calendar.getInstance();
      int mHour = c.get(Calendar.HOUR);
      int mMin = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this,
          (view, hourOfDay, minute) -> {
            binding.createPartyBtnSelectEndTime
                .setText(String.format("%02d:%02d", hourOfDay, minute));
            endTime = new MyTime(hourOfDay, minute);
          }, mHour, mMin, false);
      timePickerDialog.show();
    });
  }

  // 최대 인원 수 버튼 클릭한 경우,
  private void setMemberMaxNumClickListener() {
    binding.createPartyBtnMemberNumLeft.setOnClickListener(v -> {
      if (memMax > 2) {
        memMax--;
        binding.createPartyTvMemMaxNum.setText(memMax + "명");
      }
    });
    binding.createPartyBtnMemberNumRight.setOnClickListener(v -> {
      if (memMax < 5) {
        memMax++;
        binding.createPartyTvMemMaxNum.setText(memMax + "명");
      }
    });
  }

  private void setCreatePartyBtnClickListener() {
    binding.createPartyBtnCreateParty.setOnClickListener(v -> {
      if (hostId == null ||
          groupName == null ||
          gender == null ||
          genreList.isEmpty() ||
          meetingDate == null ||
          startTime == null ||
          endTime == null ||
          memMax == 0 ||
          karaokeId == null ||
          karaokeName == null) {
        binding.createPartyTvErrorMessage.setText("입력되지 않은 항목이 있습니다.");
      } else {
        binding.createPartyTvErrorMessage.setText("모임 생성 중");
        writeNewGroup();
      }
    });
  }

  private void writeNewGroup() {
    Party party = new Party(
        hostId,
        groupName,
        genreList,
        gender,
        age,
        ageDetail,
        karaokeId,
        karaokeName,
        meetingDate,
        1,
        memMax,
        startTime,
        endTime);
    fireDb.collection(PARTY_COLLECTION_PATH)
        .add(party)
        .addOnSuccessListener(unused -> Log.d(TAG, "SUCCESS."))
        .addOnFailureListener(e -> Log.w(TAG, "FAILURE : " + e.getMessage()));
    Joins joins = new Joins(hostId, hostId, groupName);
    fireDb.collection(JOINS_COLLECTION_PATH)
        .add(joins)
        .addOnSuccessListener(unused -> Utilities.log(LogType.d, "SUCCESS."))
        .addOnFailureListener(error -> Utilities.log(LogType.w, "FAILURE : " + error.getMessage()));
    finish();
  }

}