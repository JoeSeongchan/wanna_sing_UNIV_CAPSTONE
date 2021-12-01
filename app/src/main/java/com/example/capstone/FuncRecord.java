package com.example.capstone;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    MediaRecorder recorder;
    String filePath;

    //firebase & id
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userid="IOxZt7qFPxdE93PNtElzSztmYpP2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordAudio();
            }
        });

        findViewById(R.id.recordStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                finish();
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
}
