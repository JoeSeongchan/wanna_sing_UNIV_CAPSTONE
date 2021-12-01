package com.android.wannasing.creategroup;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.R;
import com.android.wannasing.databinding.ActivityCreateGroupBinding;
import com.android.wannasing.db.entity.Party;
import com.android.wannasing.db.entity.Party.AgeDetail;
import com.android.wannasing.db.entity.Party.Gender;
import com.android.wannasing.db.entity.Party.Genre;
import com.android.wannasing.db.entity.Party.MyTime;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;


public class CreateGroupActivity extends AppCompatActivity {

  private final List<Genre> genreList = new ArrayList<>();
  private final List<AgeDetail> ageDetail = new ArrayList<>();
  private ActivityCreateGroupBinding binding;
  private String hostId = "dummy_host_id";
  private FirebaseFirestore fireDb;
  private String groupName = null;
  private Gender gender = null;
  private int age = 0;
  private Date meetingDate = null;
  private MyTime startTime = null;
  private MyTime endTime = null;
  private int memMax = 0;
  private String karaokeId = null;
  private String karaokeName = null;

  public void onCreate(Bundle savedInstanceState) {
    binding = ActivityCreateGroupBinding
        .inflate(getLayoutInflater());
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());
    setDb();
    setUi();
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
    getKaraokeIdFromFormerActivity();
  }

  private void setPartyTitleLayout() {
    // party 제목 입력창 최대 글자 수 설정.
    binding.createGroupTilGroupTitle.setCounterEnabled(true);
    binding.createGroupTilGroupTitle.setCounterMaxLength(10);
    binding.createGroupEtGroupTitle.addTextChangedListener(new TextWatcher() {
      final TextInputLayout layout = binding.createGroupTilGroupTitle;

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
    binding.createGroupBtnGenreFree.setOnClickListener(v -> {
      binding.createGroupBtnGenreBalad.setSelected(false);
      binding.createGroupBtnGenreHiphop.setSelected(false);
      binding.createGroupBtnGenreJpop.setSelected(false);
      binding.createGroupBtnGenreOldSong.setSelected(false);
      binding.createGroupBtnGenreNormalSong.setSelected(false);
      binding.createGroupBtnGenreOther.setSelected(false);
      binding.createGroupBtnGenrePop.setSelected(false);
      binding.createGroupBtnGenreTop.setSelected(false);
      binding.createGroupBtnGenreFree.setSelected(true);
      genreList.clear();
      genreList.add(Genre.FREE);
    });

    binding.createGroupBtnGenreBalad.setOnClickListener(v -> {
      // 기존에 사용자가 버튼을 이미 클릭한 경우,
      if (binding.createGroupBtnGenreBalad.isSelected()) {
        // 해당 버튼 select 해제.
        binding.createGroupBtnGenreBalad.setSelected(false);
        // balad genre 제거.
        genreList.remove(Genre.BALAD);
      }
      // 사용자가 처음 버튼을 클릭한 경우
      else {
        binding.createGroupBtnGenreFree.setSelected(false);
        // 해당 버튼 select 설정.
        binding.createGroupBtnGenreBalad.setSelected(true);
        // free genre 해제.
        genreList.remove(Genre.FREE);
        // balad genre 추가.
        genreList.add(Genre.BALAD);
      }
    });
    binding.createGroupBtnGenreHiphop.setOnClickListener(v -> {
      if (binding.createGroupBtnGenreHiphop.isSelected()) {
        binding.createGroupBtnGenreHiphop.setSelected(false);
        genreList.remove(Genre.HIPHOP);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenreHiphop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.HIPHOP);
      }
    });
    binding.createGroupBtnGenreJpop.setOnClickListener(v -> {
      if (binding.createGroupBtnGenreJpop.isSelected()) {
        binding.createGroupBtnGenreJpop.setSelected(false);
        genreList.remove(Genre.JPOP);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenreJpop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.JPOP);
      }
    });
    binding.createGroupBtnGenreOldSong.setOnClickListener(v -> {

      if (binding.createGroupBtnGenreOldSong.isSelected()) {
        binding.createGroupBtnGenreOldSong.setSelected(false);
        genreList.remove(Genre.OLD_SONG);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenreOldSong.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.OLD_SONG);
      }
    });
    binding.createGroupBtnGenreNormalSong.setOnClickListener(v -> {
      if (binding.createGroupBtnGenreNormalSong.isSelected()) {
        binding.createGroupBtnGenreNormalSong.setSelected(false);
        genreList.remove(Genre.NORMAL_SONG);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenreNormalSong.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.NORMAL_SONG);
      }
    });
    binding.createGroupBtnGenreOther.setOnClickListener(v -> {
      if (binding.createGroupBtnGenreOther.isSelected()) {
        binding.createGroupBtnGenreOther.setSelected(false);
        genreList.remove(Genre.OTHER);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenreOther.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.OTHER);
      }
    });
    binding.createGroupBtnGenrePop.setOnClickListener(v -> {
      if (binding.createGroupBtnGenrePop.isSelected()) {
        binding.createGroupBtnGenrePop.setSelected(false);
        genreList.remove(Genre.POP);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenrePop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.POP);
      }
    });
    binding.createGroupBtnGenreTop.setOnClickListener(v -> {
      if (binding.createGroupBtnGenreTop.isSelected()) {
        binding.createGroupBtnGenreTop.setSelected(false);
        genreList.remove(Genre.TOP);
      } else {
        binding.createGroupBtnGenreFree.setSelected(false);
        binding.createGroupBtnGenreTop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.TOP);
      }
    });
  }

  // '성별' 버튼 클릭한 경우,
  private void setGenderBtnClickListener() {
    binding.createGroupBtnGenderFree.setOnClickListener(v -> {
      v.setSelected(true);
      binding.createGroupBtnGenderMale.setSelected(false);
      binding.createGroupBtnGenderFemale.setSelected(false);
      gender = Gender.FREE;
    });
    binding.createGroupBtnGenderMale.setOnClickListener(v -> {
      binding.createGroupBtnGenderFree.setSelected(false);
      v.setSelected(true);
      binding.createGroupBtnGenderFemale.setSelected(false);
      gender = Gender.MALE;
    });
    binding.createGroupBtnGenderFemale.setOnClickListener(v -> {
      binding.createGroupBtnGenderFree.setSelected(false);
      binding.createGroupBtnGenderMale.setSelected(false);
      v.setSelected(true);
      gender = Gender.FEMALE;
    });
  }

  // '나이' 버튼 클릭한 경우,
  private void setAgeBtnClickListener() {
    binding.createGroupTvAge.setText("나이 자유");
    // 왼쪽 화살표 버튼 클릭한 경우,
    binding.createGroupBtnAgeLeft.setOnClickListener(v -> {
      if (age > 0) {
        age -= 10;
        if (age > 0) {
          binding.createGroupTvAge
              .setText(getString(R.string.createGroup_tv_age, age));
        } else if (age == 0) {
          binding.createGroupTvAge.setText("나이 자유");
          binding.createGroupBtnAgeEarly.setSelected(false);
          binding.createGroupBtnAgeMid.setSelected(false);
          binding.createGroupBtnAgeLate.setSelected(false);
          ageDetail.clear();
        }
      }
    });
    // 오른쪽 화살표 버튼 클릭한 경우,
    binding.createGroupBtnAgeRight.setOnClickListener(v -> {
      if (age < 90) {
        age += 10;
        binding.createGroupTvAge.setText(getString(R.string.createGroup_tv_age, age));
      }
    });
    // '초반 버튼' 클릭한 경우,
    binding.createGroupBtnAgeEarly.setOnClickListener(v -> {
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
    binding.createGroupBtnAgeMid.setOnClickListener(v -> {
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
    binding.createGroupBtnAgeLate.setOnClickListener(v -> {
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
    binding.createGroupBtnSelectDate.setOnClickListener(v -> {
      final Calendar c = Calendar.getInstance();
      int mYear = c.get(Calendar.YEAR);
      int mMonth = c.get(Calendar.MONTH);
      int mDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this,
          (view, year, month, dayOfMonth) -> {
            binding.createGroupBtnSelectDate
                .setText(year + " / " + (month + 1) + " / " + dayOfMonth);
            meetingDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
          }, mYear, mMonth, mDay);
      datePickerDialog.show();
    });
  }

  // 시간 picker 버튼 클릭한 경우,
  private void setTimeClickListener() {
    binding.createGroupBtnSelectStartTime.setOnClickListener(v -> {
      final Calendar c = Calendar.getInstance();
      int mHour = c.get(Calendar.HOUR);
      int mMin = c.get(Calendar.MINUTE);
      TimePickerDialog timePickerDialog =
          new TimePickerDialog(this,
              (view, hourOfDay, minute) -> {
                binding.createGroupBtnSelectStartTime
                    .setText(String.format("%02d:%02d", hourOfDay, minute));
                startTime = new MyTime(hourOfDay, minute);
              }, mHour, mMin, false);
      timePickerDialog.show();
    });
    binding.createGroupBtnSelectEndTime.setOnClickListener(v -> {
      final Calendar c = Calendar.getInstance();
      int mHour = c.get(Calendar.HOUR);
      int mMin = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this,
          (view, hourOfDay, minute) -> {
            binding.createGroupBtnSelectEndTime
                .setText(String.format("%02d:%02d", hourOfDay, minute));
            endTime = new MyTime(hourOfDay, minute);
          }, mHour, mMin, false);
      timePickerDialog.show();
    });
  }

  // 최대 인원 수 버튼 클릭한 경우,
  private void setMemberMaxNumClickListener() {
    binding.createGroupBtnMemberNumLeft.setOnClickListener(v -> {
      if (memMax > 2) {
        memMax--;
        binding.createGroupTvMemMaxNum.setText(memMax + "명");
      }
    });
    binding.createGroupBtnMemberNumRight.setOnClickListener(v -> {
      if (memMax < 5) {
        memMax++;
        binding.createGroupTvMemMaxNum.setText(memMax + "명");
      }
    });
  }

  private void setCreatePartyBtnClickListener() {
    binding.createGroupBtnCreateGroup.setOnClickListener(v -> {
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
        binding.createGroupTvErrorMessage.setText("입력되지 않은 항목이 있습니다.");
      } else {
        binding.createGroupTvErrorMessage.setText("모임 생성 중");
        writeNewGroup();
      }
    });
  }

  private void getKaraokeIdFromFormerActivity() {
    karaokeId = Optional
        .ofNullable(getIntent()).map(Intent::getExtras)
        .map(bundle -> bundle.getString("KARAOKE_ID"))
        .orElse("default_karaoke_id");
    karaokeName = Optional
        .ofNullable(getIntent()).map(Intent::getExtras)
        .map(bundle -> bundle.getString("KARAOKE_NAME"))
        .orElse("default_karaoke_name");
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
        0,
        memMax,
        startTime,
        endTime);
    fireDb.collection("party_list")
        .add(party)
        .addOnSuccessListener(unused -> Log.d(TAG, "SUCCESS!"))
        .addOnFailureListener(e -> Log.w(TAG, "FAILURE : " + e.getMessage()));
  }

}