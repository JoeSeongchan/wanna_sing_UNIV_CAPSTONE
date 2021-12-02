package com.android.wannasing.feature.party.searchparty.viewcontroller;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.databinding.ActivitySearchPartyBinding;
import com.android.wannasing.feature.party.searchparty.model.PartySearchCondition;
import com.android.wannasing.feature.party.searchparty.model.PartySearchCondition.AgeDetail;
import com.android.wannasing.feature.party.searchparty.model.PartySearchCondition.Gender;
import com.android.wannasing.feature.party.searchparty.model.PartySearchCondition.Genre;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class SearchPartyActivity extends AppCompatActivity {

  // 입력 값 저장 공간.
  private final List<Genre> genreList = new ArrayList<>();
  private final List<AgeDetail> ageDetail = new ArrayList<>();
  private String groupName = null;
  private Gender gender = null;
  private int age = 0;

  // db.
  private FirebaseFirestore fireDb;

  private ActivitySearchPartyBinding binding;

  public void onCreate(Bundle savedInstanceState) {
    binding = ActivitySearchPartyBinding
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
    setSearchPartyBtnClickListener();
  }

  // 검색어(제목)를 입력한 경우
  private void setPartyTitleLayout() {
    // party 제목 입력창 최대 글자 수 설정.
    binding.searchPartyTilGroupTitle.setCounterEnabled(true);
    binding.searchPartyTilGroupTitle.setCounterMaxLength(10);
    binding.searchPartyEtGroupTitle.addTextChangedListener(new TextWatcher() {
      final TextInputLayout layout = binding.searchPartyTilGroupTitle;

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
          layout.setError("검색어를 입력해주세요. ");
        } else {
          layout.setErrorEnabled(false);
          groupName = s.toString();
        }
      }
    });
  }

  // 장르 버튼 클릭한 경우,
  private void setGenreBtnClickListener() {
    binding.searchPartyBtnGenreFree.setOnClickListener(v -> {
      binding.searchPartyBtnGenreBalad.setSelected(false);
      binding.searchPartyBtnGenreHiphop.setSelected(false);
      binding.searchPartyBtnGenreJpop.setSelected(false);
      binding.searchPartyBtnGenreOldSong.setSelected(false);
      binding.searchPartyBtnGenreNormalSong.setSelected(false);
      binding.searchPartyBtnGenreOther.setSelected(false);
      binding.searchPartyBtnGenrePop.setSelected(false);
      binding.searchPartyBtnGenreTop.setSelected(false);
      binding.searchPartyBtnGenreFree.setSelected(true);
      genreList.add(Genre.FREE);
    });

    binding.searchPartyBtnGenreBalad.setOnClickListener(v -> {
      // 기존에 사용자가 버튼을 이미 클릭한 경우,
      if (binding.searchPartyBtnGenreBalad.isSelected()) {
        // 해당 버튼 select 해제.
        binding.searchPartyBtnGenreBalad.setSelected(false);
        // balad genre 제거.
        genreList.remove(Genre.BALAD);
      }
      // 사용자가 처음 버튼을 클릭한 경우
      else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        // 해당 버튼 select 설정.
        binding.searchPartyBtnGenreBalad.setSelected(true);
        // free genre 해제.
        genreList.remove(Genre.FREE);
        // balad genre 추가.
        genreList.add(Genre.BALAD);
      }
    });
    binding.searchPartyBtnGenreHiphop.setOnClickListener(v -> {
      if (binding.searchPartyBtnGenreHiphop.isSelected()) {
        binding.searchPartyBtnGenreHiphop.setSelected(false);
        genreList.remove(Genre.HIPHOP);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenreHiphop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.HIPHOP);
      }
    });
    binding.searchPartyBtnGenreJpop.setOnClickListener(v -> {
      if (binding.searchPartyBtnGenreJpop.isSelected()) {
        binding.searchPartyBtnGenreJpop.setSelected(false);
        genreList.remove(Genre.JPOP);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenreJpop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.JPOP);
      }
    });
    binding.searchPartyBtnGenreOldSong.setOnClickListener(v -> {

      if (binding.searchPartyBtnGenreOldSong.isSelected()) {
        binding.searchPartyBtnGenreOldSong.setSelected(false);
        genreList.remove(Genre.OLD_SONG);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenreOldSong.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.OLD_SONG);
      }
    });
    binding.searchPartyBtnGenreNormalSong.setOnClickListener(v -> {
      if (binding.searchPartyBtnGenreNormalSong.isSelected()) {
        binding.searchPartyBtnGenreNormalSong.setSelected(false);
        genreList.remove(Genre.NORMAL_SONG);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenreNormalSong.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.NORMAL_SONG);
      }
    });
    binding.searchPartyBtnGenreOther.setOnClickListener(v -> {
      if (binding.searchPartyBtnGenreOther.isSelected()) {
        binding.searchPartyBtnGenreOther.setSelected(false);
        genreList.remove(Genre.OTHER);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenreOther.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.OTHER);
      }
    });
    binding.searchPartyBtnGenrePop.setOnClickListener(v -> {
      if (binding.searchPartyBtnGenrePop.isSelected()) {
        binding.searchPartyBtnGenrePop.setSelected(false);
        genreList.remove(Genre.POP);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenrePop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.POP);
      }
    });
    binding.searchPartyBtnGenreTop.setOnClickListener(v -> {
      if (binding.searchPartyBtnGenreTop.isSelected()) {
        binding.searchPartyBtnGenreTop.setSelected(false);
        genreList.remove(Genre.TOP);
      } else {
        binding.searchPartyBtnGenreFree.setSelected(false);
        binding.searchPartyBtnGenreTop.setSelected(true);
        genreList.remove(Genre.FREE);
        genreList.add(Genre.TOP);
      }
    });
  }

  // '성별' 버튼 클릭한 경우,
  private void setGenderBtnClickListener() {
    binding.searchPartyBtnGenderFree.setOnClickListener(v -> {
      v.setSelected(true);
      binding.searchPartyBtnGenderMale.setSelected(false);
      binding.searchPartyBtnGenderFemale.setSelected(false);
      gender = Gender.FREE;
    });
    binding.searchPartyBtnGenderMale.setOnClickListener(v -> {
      binding.searchPartyBtnGenderFree.setSelected(false);
      v.setSelected(true);
      binding.searchPartyBtnGenderFemale.setSelected(false);
      gender = Gender.MALE;
    });
    binding.searchPartyBtnGenderFemale.setOnClickListener(v -> {
      binding.searchPartyBtnGenderFree.setSelected(false);
      binding.searchPartyBtnGenderMale.setSelected(false);
      v.setSelected(true);
      gender = Gender.FEMALE;
    });
  }

  // '나이' 버튼 클릭한 경우,
  private void setAgeBtnClickListener() {
    binding.searchPartyTvAge.setText("나이 자유");
    // 왼쪽 화살표 버튼 클릭한 경우,
    binding.searchPartyBtnAgeLeft.setOnClickListener(v -> {
      if (age > 0) {
        age -= 10;
        if (age > 0) {
          binding.searchPartyTvAge.setText(age + "대");
        } else if (age == 0) {
          binding.searchPartyTvAge.setText("나이 자유");
          binding.searchPartyBtnAgeEarly.setSelected(false);
          binding.searchPartyBtnAgeMid.setSelected(false);
          binding.searchPartyBtnAgeLate.setSelected(false);
          ageDetail.clear();
        }
      }
    });
    // 오른쪽 화살표 버튼 클릭한 경우,
    binding.searchPartyBtnAgeRight.setOnClickListener(v -> {
      if (age < 90) {
        age += 10;
        binding.searchPartyTvAge.setText(age + "대");
      }
    });
    // '초반 버튼' 클릭한 경우,
    binding.searchPartyBtnAgeEarly.setOnClickListener(v -> {
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
    binding.searchPartyBtnAgeMid.setOnClickListener(v -> {
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
    binding.searchPartyBtnAgeLate.setOnClickListener(v -> {
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

  private void setSearchPartyBtnClickListener() {
    binding.searchPartyBtnSearch.setOnClickListener(v -> {
      if (groupName == null ||
          gender == null ||
          genreList.isEmpty()) {
        binding.searchPartyTvError.setText("선택된 항목이 없습니다.");
      } else {
        binding.searchPartyTvError.setText("검색 중");
        // send to 검색 결과를 보여주는 Activity 에게 검색 조건 전달.
        PartySearchCondition condition = makeConditionForSearchingParties();
        Intent intent = new Intent();
        intent.putExtra("CONDITION_FOR_SEARCHING_PARTIES", condition);
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }

  private PartySearchCondition makeConditionForSearchingParties() {
    PartySearchCondition condition = new PartySearchCondition(
        groupName,
        genreList,
        gender,
        age,
        ageDetail);
    return condition;
  }
}
