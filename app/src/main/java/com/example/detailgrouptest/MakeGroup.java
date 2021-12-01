package com.example.detailgrouptest;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.detailgrouptest.DB.GroupDb;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MakeGroup extends AppCompatActivity {

  Button btn_selectDate, btn_selectStarttime, btn_selectEndtime, btn_age, btn_groupP,
      btn_genre01, btn_genre02, btn_genre03, btn_genre04,
      btn_genre05, btn_genre06, btn_genre07, btn_genre08,
      btn_genre09, btn_genre10, btn_genre11, btn_genre12,
      btn_genderFree, btn_genderM, btn_genderF,
      btn_ageEarly, btn_ageMid, btn_ageLate,
      btn_createGroup;

  ImageButton btn_ageleft, btn_ageright, btn_groupPleft, btn_groupPrihgt;
  DatePickerDialog datePickerDialog;
  TimePickerDialog timePickerDialog;
  TextView textDate, errorMes;
  TextClock textStarttime, textEndtime;
  //서버에 보낼 정보
  String hostID = "HOSTID";
  String groupName = "";   //모임 이름
  int[] gener = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  String gender = "셩별 자유";  //제한조건 성별 자유(기본값), 남자만, 여자만
  int age = 0;    //나이대
  List<Integer> ageSub = new ArrayList<>();  //나이대 초중후
  int groupP = 2;
  String SingRoomID = "SINGROOMID";   //노래방코드
  String SingRoomName = "노래방 이름";    //노래방이름
  String groupDate;   //모임날짜
  int groupStartTime_hour;  //모임시작시간
  int groupStartTime_min;
  int groupEndTime_hour;    //모임종료시간
  int groupEndTime_min;
  //오류여부 확인
  boolean errorCheck = false;
  int[] error = {0, 0, 0, 0};
  private FirebaseFirestore mDatabase;

    /*
    error[0] = 모임이름 입력여부
    error[1] = 모임날짜 선택여부
    error[2] = 모임시작시간 선택여부
    error[3] = 모임종료시간 선택여부
     */

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.create_group);

    TextInputLayout inputLayout = findViewById(R.id.groupname);
    EditText editText = findViewById(R.id.groupnameText);

    //최대 글자수 설정
    inputLayout.setCounterEnabled(true);
    inputLayout.setCounterMaxLength(10);

    //firebase
    mDatabase = FirebaseFirestore.getInstance();

    //모임명 입력
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
          inputLayout.setError("모임명을 입력해주세요.");
        } else {
          groupName = s.toString();
          inputLayout.setError(null); //에러 메시지를 지워주는 기능
        }
      }
    });

    //버튼
    //장르 버튼
    btn_genre01 = findViewById(R.id.genre01);   //자유
    btn_genre02 = findViewById(R.id.genre02);   //인기차트
    btn_genre03 = findViewById(R.id.genre03);   //발라드
    btn_genre04 = findViewById(R.id.genre04);   //가요
    btn_genre05 = findViewById(R.id.genre05);   //pop
    btn_genre06 = findViewById(R.id.genre06);   //트로트
    btn_genre07 = findViewById(R.id.genre07);   //힙합
    btn_genre08 = findViewById(R.id.genre08);   //j-pop
    btn_genre09 = findViewById(R.id.genre09);   //장르1
    btn_genre10 = findViewById(R.id.genre10);   //장르2
    btn_genre11 = findViewById(R.id.genre11);   //장르3
    btn_genre12 = findViewById(R.id.genre12);   //그 외

    //모임일시 선택
    btn_selectDate = findViewById(R.id.selectDate);
    btn_selectStarttime = findViewById(R.id.selectStarttime);
    btn_selectEndtime = findViewById(R.id.selectEndtime);

    btn_age = findViewById(R.id.age);
    btn_groupP = findViewById(R.id.groupP);

    //성별조건
    btn_genderFree = findViewById(R.id.gender_free);    //성별자유
    btn_genderM = findViewById(R.id.gender_M);    //남자만
    btn_genderF = findViewById(R.id.gender_F);    //여자만

    //나이대 조절버튼
    btn_ageleft = findViewById(R.id.age_left);
    btn_ageright = findViewById(R.id.age_right);

    //나이대 세부사항
    btn_ageEarly = findViewById(R.id.age_early);    //초반
    btn_ageMid = findViewById(R.id.age_mid);        //중반
    btn_ageLate = findViewById(R.id.age_late);      //후반

    //모임 인원 조절 버튼
    btn_groupPleft = findViewById(R.id.groupP_left);
    btn_groupPrihgt = findViewById(R.id.groupP_right);

    //방만들기 버튼
    btn_createGroup = findViewById(R.id.createGroup);
    //에러메세지
    errorMes = findViewById(R.id.errorMessage);

    //장르
    btn_genre01.setOnClickListener(this::onClick);
    btn_genre01.setSelected(true);
    btn_genre02.setOnClickListener(this::onClick);
    btn_genre03.setOnClickListener(this::onClick);
    btn_genre04.setOnClickListener(this::onClick);
    btn_genre05.setOnClickListener(this::onClick);
    btn_genre06.setOnClickListener(this::onClick);
    btn_genre07.setOnClickListener(this::onClick);
    btn_genre08.setOnClickListener(this::onClick);
    btn_genre09.setOnClickListener(this::onClick);
    btn_genre10.setOnClickListener(this::onClick);
    btn_genre11.setOnClickListener(this::onClick);
    btn_genre12.setOnClickListener(this::onClick);

    //성별조건
    btn_genderFree.setOnClickListener(this::onClick);
    btn_genderFree.setSelected(true);
    btn_genderM.setOnClickListener(this::onClick);
    btn_genderF.setOnClickListener(this::onClick);

    //나이대관련
    btn_ageleft.setOnClickListener(this::onClick);  //조절화살표
    btn_ageright.setOnClickListener(this::onClick); //조절화살표
    btn_ageEarly.setOnClickListener(this::onClick); //초반
    btn_ageMid.setOnClickListener(this::onClick);   //중반
    btn_ageLate.setOnClickListener(this::onClick);  //후반

    //모임일시선택
    btn_selectDate.setOnClickListener(this::onClick);
    btn_selectStarttime.setOnClickListener(this::onClick);
    btn_selectEndtime.setOnClickListener(this::onClick);

    //모임인원선택
    btn_groupPleft.setOnClickListener(this::onClick);
    btn_groupPrihgt.setOnClickListener(this::onClick);

    //방만들기
    btn_createGroup.setOnClickListener(this::onClick);
  }

  public void onClick(View view) {

    //장르 선택
    //자유롭게
    if (view == btn_genre01) {
      btn_genre01.setSelected(!btn_genre01.isSelected());

      if (btn_genre01.isSelected()) {
        btn_genre02.setSelected(false);
        btn_genre03.setSelected(false);
        btn_genre04.setSelected(false);
        btn_genre05.setSelected(false);
        btn_genre06.setSelected(false);
        btn_genre07.setSelected(false);
        btn_genre08.setSelected(false);
        btn_genre09.setSelected(false);
        btn_genre10.setSelected(false);
        btn_genre11.setSelected(false);
        btn_genre12.setSelected(false);
        gener[0] = 1;
      }
    }
    //인기차트
    if (view == btn_genre02) {
      btn_genre02.setSelected(!btn_genre02.isSelected());

      if (btn_genre02.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[1] = 1;
      }
    }
    //발라드
    if (view == btn_genre03) {
      btn_genre03.setSelected(!btn_genre03.isSelected());

      if (btn_genre03.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[2] = 1;
      }
    }
    //가요
    if (view == btn_genre04) {
      btn_genre04.setSelected(!btn_genre04.isSelected());

      if (btn_genre04.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[3] = 1;
      }
    }
    //pop
    if (view == btn_genre05) {
      btn_genre05.setSelected(!btn_genre05.isSelected());

      if (btn_genre05.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[4] = 1;
      }
    }
    //트로트
    if (view == btn_genre06) {
      btn_genre06.setSelected(!btn_genre06.isSelected());

      if (btn_genre06.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[5] = 1;
      }
    }
    //힙합
    if (view == btn_genre07) {
      btn_genre07.setSelected(!btn_genre07.isSelected());

      if (btn_genre07.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[6] = 1;
      }
    }
    //J-pop
    if (view == btn_genre08) {
      btn_genre08.setSelected(!btn_genre08.isSelected());

      if (btn_genre08.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[7] = 1;
      }
    }
    //장르1
    if (view == btn_genre09) {
      btn_genre09.setSelected(!btn_genre09.isSelected());

      if (btn_genre09.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[8] = 1;
      }
    }
    //장르2
    if (view == btn_genre10) {
      btn_genre10.setSelected(!btn_genre10.isSelected());

      if (btn_genre10.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[9] = 1;
      }
    }
    //장르3
    if (view == btn_genre11) {
      btn_genre11.setSelected(!btn_genre11.isSelected());

      if (btn_genre11.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[10] = 1;
      }
    }
    //그 외
    if (view == btn_genre12) {
      btn_genre12.setSelected(!btn_genre12.isSelected());

      if (btn_genre12.isSelected()) {
        btn_genre01.setSelected(false);
        gener[0] = 0;
        gener[11] = 1;
      }
    }

    //성별제한 선택
    if (view == btn_genderFree) {
      btn_genderFree.setSelected(!btn_genderFree.isSelected());
      btn_genderM.setSelected(false);
      btn_genderF.setSelected(false);

      if (btn_genderFree.isSelected()) {
        gender = "성별 자유";
      }
    }
    if (view == btn_genderM) {
      btn_genderM.setSelected(!btn_genderM.isSelected());
      btn_genderFree.setSelected(false);
      btn_genderF.setSelected(false);

      if (btn_genderM.isSelected()) {
        gender = "남자만";
      }
    }
    if (view == btn_genderF) {
      btn_genderF.setSelected(!btn_genderF.isSelected());
      btn_genderFree.setSelected(false);
      btn_genderM.setSelected(false);

      if (btn_genderF.isSelected()) {
        gender = "여자만";
      }
    }

    //나이대 선택
    if (view == btn_ageleft) {
      if (age < 10) {
        btn_age.setText("나이 자유");
      } else if (age > 0) {
        age = age - 10;
        if (age > 0) {
          btn_age.setText(age + "대");
        } else if (age == 0) {
          btn_age.setText("나이 자유");
          btn_ageEarly.setSelected(false);
          btn_ageMid.setSelected(false);
          btn_ageLate.setSelected(false);
          ageSub[0] = 0;
          ageSub[1] = 0;
          ageSub[2] = 0;
        }
      }
    }
    if (view == btn_ageright) {
      if (age < 90) {
        age = age + 10;
        btn_age.setText(age + "대");
      }
    }

    //나이대 세부사항(초중후반) 선택
    if (view == btn_ageEarly) {
      if (age != 0) {
        btn_ageEarly.setSelected(!btn_ageEarly.isSelected());
        ageSub[0] = 1;
      }
    }
    if (view == btn_ageMid) {
      if (age != 0) {
        btn_ageMid.setSelected(!btn_ageMid.isSelected());
        ageSub[1] = 1;
      }
    }
    if (view == btn_ageLate) {
      if (age != 0) {
        btn_ageLate.setSelected(!btn_ageLate.isSelected());
        ageSub[2] = 1;
      }
    }

    //모임 날짜 선택
    if (view == btn_selectDate) {
      final Calendar c = Calendar.getInstance();
      int mYear = c.get(Calendar.YEAR);
      int mMonth = c.get(Calendar.MONTH);
      int mDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this,
          new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
              btn_selectDate.setText(year + " / " + (month + 1) + " / " + dayOfMonth);
              groupDate = year + " / " + (month + 1) + " / " + dayOfMonth;
              error[1] = 1;   //선택확인
            }
          }, mYear, mMonth, mDay);
      datePickerDialog.show();
    }

    //모임 시작시간 선택
    if (view == btn_selectStarttime) {
      final Calendar c = Calendar.getInstance();
      int mHour = c.get(Calendar.HOUR);
      int mMin = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this,
          new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
              btn_selectStarttime.setText(String.format("%02d:%02d", hourOfDay, minute));
              groupStartTime_hour = hourOfDay;
              groupStartTime_min = minute;
              error[2] = 1;   //선택확인
            }
          }, mHour, mMin, false);
      timePickerDialog.show();
    }

    //모임 종료시간 선택
    if (view == btn_selectEndtime) {
      final Calendar c = Calendar.getInstance();
      int mHour = c.get(Calendar.HOUR);
      int mMin = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this,
          new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
              btn_selectEndtime.setText(String.format("%02d:%02d", hourOfDay, minute));
              groupEndTime_hour = hourOfDay;
              groupEndTime_min = minute;
              error[3] = 1;   //선택확인
            }
          }, mHour, mMin, false);
      timePickerDialog.show();
    }

    //그룹 인원 선택
    if (view == btn_groupPleft) {
      if (groupP > 2) {
        groupP = groupP - 1;
        btn_groupP.setText(groupP + "명");
      }
    }
    if (view == btn_groupPrihgt) {
      if (groupP < 5) {
        groupP = groupP + 1;
        btn_groupP.setText(groupP + "명");
      }
    }

    //방만들기
    if (view == btn_createGroup) {
      //Log.e(TAG, "\n\n\n뭐지???");
      if (!groupName.isEmpty()) {
        error[0] = 1;
      }

      //입력하지 않은 항목 확인
      errorCheck = false;
      for (int i = 0; i < 4; i++) {
        if (error[i] == 0) {
          errorCheck = true;
        }
      }

      //입력하지 않은 항목이 존재한다면
      if (errorCheck) {
        errorMes.setText("입력되지 않은 항목이 있습니다.");
        Log.e(TAG, "\n에러상황 :" + error + "\n\n");
      } else {
        //데이터베이스로 보내고 세부사항 창으로 이동하기
        errorMes.setText("모임 생성 중");
        Log.e(TAG, "\n-------결과출력---------\n모임제목 : " + groupName
            + "\n장르선택 : " + gener + "\n성별조건 : " + gender + "나이대 : " + age
            + "\n초중후반 : " + ageSub + "\n노래방 이름" + SingRoomName + "\n모임 날짜 : " + groupDate
            + "\n시작시간 : " + groupStartTime_hour + "\n종료시간 : " + groupEndTime_hour);

        writeNewGroup(hostID, groupName, gener, gender, age, ageSub, groupP, SingRoomID,
            SingRoomName, groupDate,
            groupStartTime_hour, groupStartTime_min, groupEndTime_hour, groupEndTime_min);
      }
    }
  }

  //지도로부터 노래방 정보를 받아오는
  void getSingroomName(String ID, String name) {

    SingRoomName = name;
  }


  private void writeNewGroup(String hostID, String groupName, int[] gener, String gender, int age,
      List<Integer> ageSub,
      int groupP, String singRoomID, String singroomName, String groupDate, int StartTime_hour,
      int StartTime_min,
      int EndTime_hour, int EndTime_min) {

    GroupDb GD = new GroupDb(hostID, groupName, gener, gender, age, ageSub, groupP, singRoomID,
        singroomName, groupDate,
        StartTime_hour, StartTime_min, EndTime_hour, EndTime_min);

    mDatabase.collection("group_detail")
        .add(GD)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            // Write failed
            Log.w(TAG, "Error adding document", e);
          }
        });
  }
}
