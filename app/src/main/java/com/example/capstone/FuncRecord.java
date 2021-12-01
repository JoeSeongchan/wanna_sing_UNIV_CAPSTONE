package com.example.capstone;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FuncRecord extends AppCompatActivity {
    private MediaRecorder recorder;
    private String filePath;
    private ImageButton record;
    private TextView recording;
    private boolean isRecording = false;    // 현재 녹음 상태를 확인하기 위함.

    //firebase & id
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userid="IOxZt7qFPxdE93PNtElzSztmYpP2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        permissionCheck();
        init();
        click();
    }

    private void init(){
        record=findViewById(R.id.record_ib_record);
        recording=findViewById(R.id.record_tv_recordstatus);
    }

    private void click(){
        record.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording==false) {
                    recordAudio();
                    record.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_stop_24, null));
                    recording.setText("녹음중..");
                    recording.setTextColor(Color.parseColor("#FF0000"));
                    isRecording = true;
                }
                else{
                   stopRecording();
                   record.setImageDrawable(getResources().getDrawable(R.drawable.ic_record, null));
                   recording.setText("녹음준비");
                   recording.setTextColor(Color.parseColor("#8C8C8C"));
                   isRecording = false;
                }

            }
        });
    }





    private void recordAudio() {
        setupAudio();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 어디에서 음성 데이터를 받을 것인지
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder.setOutputFile(filePath);

        try {
            recorder.prepare();
            recorder.start();

            Toast.makeText(this, "녹음 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupAudio() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("recordDir", Context.MODE_PRIVATE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(directory, timeStamp+".mp4");
        this.filePath = file.getAbsolutePath();
        Log.d("MainActivity_R", " - setupStorage" +
                "\ntarget path : " + filePath + "\n---");
    }



    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(this, "녹음 중지 및 업로드 중..", Toast.LENGTH_SHORT).show();


            Uri fileUri = Uri.fromFile(new File(filePath));
            Log.d("upload","뭐야이건"+fileUri);

            StorageReference s_ref = storage.getReference();
            StorageReference r_ref = s_ref.child(userid+"/"+fileUri.getLastPathSegment());
            Log.d("upload","뭐야이건"+fileUri.getLastPathSegment());
            UploadTask uploadTask = r_ref.putFile((fileUri));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("upload",exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }
    }

    public void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }


}
