package com.example.capstone;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileChangePopup extends Activity {
    EditText editText;
    TextView title;
    String titlechange;
    String changed;

    //user id
    String userid = "IOxZt7qFPxdE93PNtElzSztmYpP2";

    //파이어 베이스
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected DocumentReference docref = db.collection("user_profile").document(userid);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupactivity_change_genre);


        editText = findViewById(R.id.popup_et_genre);
        Intent intent = getIntent();
        titlechange = intent.getStringExtra("option");
        title = findViewById(R.id.popup_tv_title);
        title.setText(titlechange);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        changed=editText.getText().toString();

        if(titlechange=="선호장르 변경") {
            docref
                    .update("primegenre", changed)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("UPDATE", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("UPDATE", "Error updating document", e);
                        }
                    });
            Toast.makeText(this, "프로필 업데이트 중..",Toast.LENGTH_SHORT).show();
        }
        else{
            docref
                    .update("primelocal", changed)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("UPDATE", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("UPDATE", "Error updating document", e);
                        }
                    });
            Toast.makeText(this, "프로필 업데이트 중..",Toast.LENGTH_SHORT).show();
        }
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}


