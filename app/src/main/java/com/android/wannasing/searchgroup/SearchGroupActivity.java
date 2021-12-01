package com.android.wannasing.searchgroup;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.db.entity.SearchParty;
import com.android.wannasing.databinding.ActivitySearchGroupBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;


public class SearchGroupActivity extends AppCompatActivity {

    private final List<SearchParty.Genre> genreList = new ArrayList<>();
    private final List<SearchParty.AgeDetail> ageDetail = new ArrayList<>();
    private ActivitySearchGroupBinding binding;
    //  private String hostId = "dummy_host_id";
    private FirebaseFirestore fireDb;
    private String groupName = null;
    private SearchParty.Gender gender = null;
    private int age = 0;


    public void onCreate(Bundle savedInstanceState) {
        binding = ActivitySearchGroupBinding
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
        binding.searchGroupTilGroupTitle.setCounterEnabled(true);
        binding.searchGroupTilGroupTitle.setCounterMaxLength(10);
        binding.searchGroupEtGroupTitle.addTextChangedListener(new TextWatcher() {
            final TextInputLayout layout = binding.searchGroupTilGroupTitle;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                } else {
                    groupName = s.toString();
                }
            }
        });
    }

    // 장르 버튼 클릭한 경우,
    private void setGenreBtnClickListener() {
        binding.searchGroupBtnGenreFree.setOnClickListener(v -> {
            binding.searchGroupBtnGenreBalad.setSelected(false);
            binding.searchGroupBtnGenreHiphop.setSelected(false);
            binding.searchGroupBtnGenreJpop.setSelected(false);
            binding.searchGroupBtnGenreOldSong.setSelected(false);
            binding.searchGroupBtnGenreNormalSong.setSelected(false);
            binding.searchGroupBtnGenreOther.setSelected(false);
            binding.searchGroupBtnGenrePop.setSelected(false);
            binding.searchGroupBtnGenreTop.setSelected(false);
            binding.searchGroupBtnGenreFree.setSelected(true);
            genreList.add(SearchParty.Genre.FREE);
        });

        binding.searchGroupBtnGenreBalad.setOnClickListener(v -> {
            // 기존에 사용자가 버튼을 이미 클릭한 경우,
            if (binding.searchGroupBtnGenreBalad.isSelected()) {
                // 해당 버튼 select 해제.
                binding.searchGroupBtnGenreBalad.setSelected(false);
                // balad genre 제거.
                genreList.remove(SearchParty.Genre.BALAD);
            }
            // 사용자가 처음 버튼을 클릭한 경우
            else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                // 해당 버튼 select 설정.
                binding.searchGroupBtnGenreBalad.setSelected(true);
                // free genre 해제.
                genreList.remove(SearchParty.Genre.FREE);
                // balad genre 추가.
                genreList.add(SearchParty.Genre.BALAD);
            }
        });
        binding.searchGroupBtnGenreHiphop.setOnClickListener(v -> {
            if (binding.searchGroupBtnGenreHiphop.isSelected()) {
                binding.searchGroupBtnGenreHiphop.setSelected(false);
                genreList.remove(SearchParty.Genre.HIPHOP);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenreHiphop.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.HIPHOP);
            }
        });
        binding.searchGroupBtnGenreJpop.setOnClickListener(v -> {
            if (binding.searchGroupBtnGenreJpop.isSelected()) {
                binding.searchGroupBtnGenreJpop.setSelected(false);
                genreList.remove(SearchParty.Genre.JPOP);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenreJpop.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.JPOP);
            }
        });
        binding.searchGroupBtnGenreOldSong.setOnClickListener(v -> {

            if (binding.searchGroupBtnGenreOldSong.isSelected()) {
                binding.searchGroupBtnGenreOldSong.setSelected(false);
                genreList.remove(SearchParty.Genre.OLD_SONG);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenreOldSong.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.OLD_SONG);
            }
        });
        binding.searchGroupBtnGenreNormalSong.setOnClickListener(v -> {
            if (binding.searchGroupBtnGenreNormalSong.isSelected()) {
                binding.searchGroupBtnGenreNormalSong.setSelected(false);
                genreList.remove(SearchParty.Genre.NORMAL_SONG);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenreNormalSong.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.NORMAL_SONG);
            }
        });
        binding.searchGroupBtnGenreOther.setOnClickListener(v -> {
            if (binding.searchGroupBtnGenreOther.isSelected()) {
                binding.searchGroupBtnGenreOther.setSelected(false);
                genreList.remove(SearchParty.Genre.OTHER);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenreOther.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.OTHER);
            }
        });
        binding.searchGroupBtnGenrePop.setOnClickListener(v -> {
            if (binding.searchGroupBtnGenrePop.isSelected()) {
                binding.searchGroupBtnGenrePop.setSelected(false);
                genreList.remove(SearchParty.Genre.POP);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenrePop.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.POP);
            }
        });
        binding.searchGroupBtnGenreTop.setOnClickListener(v -> {
            if (binding.searchGroupBtnGenreTop.isSelected()) {
                binding.searchGroupBtnGenreTop.setSelected(false);
                genreList.remove(SearchParty.Genre.TOP);
            } else {
                binding.searchGroupBtnGenreFree.setSelected(false);
                binding.searchGroupBtnGenreTop.setSelected(true);
                genreList.remove(SearchParty.Genre.FREE);
                genreList.add(SearchParty.Genre.TOP);
            }
        });
    }

    // '성별' 버튼 클릭한 경우,
    private void setGenderBtnClickListener() {
        binding.searchGroupBtnGenderFree.setOnClickListener(v -> {
            v.setSelected(true);
            binding.searchGroupBtnGenderMale.setSelected(false);
            binding.searchGroupBtnGenderFemale.setSelected(false);
            gender = SearchParty.Gender.FREE;
        });
        binding.searchGroupBtnGenderMale.setOnClickListener(v -> {
            binding.searchGroupBtnGenderFree.setSelected(false);
            v.setSelected(true);
            binding.searchGroupBtnGenderFemale.setSelected(false);
            gender = SearchParty.Gender.MALE;
        });
        binding.searchGroupBtnGenderFemale.setOnClickListener(v -> {
            binding.searchGroupBtnGenderFree.setSelected(false);
            binding.searchGroupBtnGenderMale.setSelected(false);
            v.setSelected(true);
            gender = SearchParty.Gender.FEMALE;
        });
    }

    // '나이' 버튼 클릭한 경우,
    private void setAgeBtnClickListener() {
        binding.searchGroupTvAge.setText("나이 자유");
        // 왼쪽 화살표 버튼 클릭한 경우,
        binding.searchGroupBtnAgeLeft.setOnClickListener(v -> {
            if (age > 0) {
                age -= 10;
                if (age > 0) {
                    binding.searchGroupTvAge.setText(age + "대");
                } else if (age == 0) {
                    binding.searchGroupTvAge.setText("나이 자유");
                    binding.searchGroupBtnAgeEarly.setSelected(false);
                    binding.searchGroupBtnAgeMid.setSelected(false);
                    binding.searchGroupBtnAgeLate.setSelected(false);
                    ageDetail.clear();
                }
            }
        });
        // 오른쪽 화살표 버튼 클릭한 경우,
        binding.searchGroupBtnAgeRight.setOnClickListener(v -> {
            if (age < 90) {
                age += 10;
                binding.searchGroupTvAge.setText(age + "대");
            }
        });
        // '초반 버튼' 클릭한 경우,
        binding.searchGroupBtnAgeEarly.setOnClickListener(v -> {
            if (age == 0) {
                return;
            }
            if (v.isSelected()) {
                v.setSelected(false);
                ageDetail.remove(SearchParty.AgeDetail.EARLY);
            } else {
                v.setSelected(true);
                ageDetail.add(SearchParty.AgeDetail.EARLY);
            }
        });
        // '중반 버튼' 클릭한 경우,
        binding.searchGroupBtnAgeMid.setOnClickListener(v -> {
            if (age == 0) {
                return;
            }
            if (v.isSelected()) {
                v.setSelected(false);
                ageDetail.remove(SearchParty.AgeDetail.MID);
            } else {
                v.setSelected(true);
                ageDetail.add(SearchParty.AgeDetail.MID);
            }
        });
        // '후반 버튼' 클릭한 경우,
        binding.searchGroupBtnAgeLate.setOnClickListener(v -> {
            if (age == 0) {
                return;
            }
            if (v.isSelected()) {
                v.setSelected(false);
                ageDetail.remove(SearchParty.AgeDetail.LATE);
            } else {
                v.setSelected(true);
                ageDetail.add(SearchParty.AgeDetail.LATE);
            }
        });
    }


    private void setSearchPartyBtnClickListener() {
        binding.searchGroupBtnSearch.setOnClickListener(v -> {
            if (groupName == null ||
                    gender == null ||
                    genreList.isEmpty()) {
                binding.searchGroupTvError.setText("선택된 항목이 없습니다.");
            } else {
                binding.searchGroupTvError.setText("검색 중");
                searchGroup();
            }
        });
    }


    private void searchGroup() {
        SearchParty searchParty = new SearchParty(
                groupName,
                genreList,
                gender,
                age,
                ageDetail);

        fireDb.collection("party_list")
                .add(party)
                .addOnSuccessListener(unused -> Log.d(TAG, "SUCCESS!"))
                .addOnFailureListener(e -> Log.w(TAG, "FAILURE : " + e.getMessage()));
    }

}
