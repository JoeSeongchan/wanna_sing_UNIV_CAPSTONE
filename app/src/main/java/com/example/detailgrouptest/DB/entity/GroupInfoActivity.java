package com.example.detailgrouptest.DB.entity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.detailgrouptest.DB.entity.Party.AgeDetail;
import com.example.detailgrouptest.DB.entity.Party.Genre;
import com.example.detailgrouptest.databinding.ActivityGroupInfoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {

  private ActivityGroupInfoBinding binding = ActivityGroupInfoBinding
      .inflate(getLayoutInflater());
  private FirebaseFirestore fireDb;
  private String GroupId = "dumy";
  private String groupName = "";
  private List<Genre> genreList;
  private String gender;
  private int age = 0;
  private List<AgeDetail> ageDetail;
  private String meetingDate;
  private String meetingTime;
  private String memMax;
  private String curMemberNum;
  private String karaokeId;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());

    // db 설정.
    fireDb = FirebaseFirestore.getInstance();

    setUi();

  }

  private void setUi() {
    readGroupInfo();

  }

  private void readGroupInfo() {
    fireDb.collection("party_list")
        .document(GroupId)
        .get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                //모임 제목
                binding.groupDetailTvTitle.setText(document.getData().get("groupName").toString());
                gender = document.getData().get("gender").toString();
                //genreList = "#" + document.getData().get("genre").toString();

                //해야하는거 : 리스트인 애들을(장르, 나이대초중후반)를 어떤식으로 받아오는가...?

                //현재인원수 / 최대인원수
                curMemberNum = document.getData().get("curMemberNum").toString();
                memMax = document.getData().get("memMax").toString();
                binding.groupDetailTvMemberNum.setText("(" + curMemberNum + " / " + memMax + ")");

                //노래방 이름
                binding.groupDetailTvKaraokeName
                    .setText(document.getData().get("karaokeName").toString());

                //모임 일시
                meetingDate = document.getData().get("meetingDate").toString();
                meetingTime = document.getData().get("startTime").toString() + " ~ "
                    + document.getData().get("endTime").toString();
                binding.groupDetailTvPartyTime.setText(meetingDate + "\n" + meetingTime);

              } else {
              }
            }
          }

        });
  }

  private void readGroupMem() {
    fireDb.collection("party_member_list")
        .document(GroupId)
        .get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if (document.exists()) {
                //멤버 정보... 어떤식으로 들어잇는거지
                binding.groupDetailTvTitle.setText(document.getData().get("groupName").toString());
                gender = document.getData().get("gender").toString();
                //genreList = "#" + document.getData().get("genre").toString();

              } else {
              }
            }
          }

        });
  }

}
