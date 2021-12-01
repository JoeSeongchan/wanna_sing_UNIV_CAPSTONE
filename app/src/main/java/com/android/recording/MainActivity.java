package com.android.recording;

import android.Manifest.permission;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  // 접근 권한 코드.
  private static final int REQ_CODE_PERMISSION = 0;

  MediaRecorder recorder = null;
  String filePath = null;
  MediaPlayer player = null;
  int position = 0;     // 다시 시작 기능을 위한 현재 재생 위치 확인 변수

  Button btnPlay, btnPause, btnRestart, btnStop, btnRecord, btnRecordStop;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // 외부 저장소 쓰기 권한 요구. 체크.
    ActivityCompat.requestPermissions(this, new String[]{permission.READ_EXTERNAL_STORAGE},
        REQ_CODE_PERMISSION);
  }

  // 권한 요청할 때 할 행동.
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQ_CODE_PERMISSION) {
      for (int grantResult : grantResults) {
        if (grantResult == PackageManager.PERMISSION_DENIED) {
          finish();
          Log.d("MainActivity_R",
              " - onRequestPermissionsResult" + "\npermission request is denied.");
          return;
        }
      }
      setupUi();        // ui 설정.
      setupStorage();   // storage 설정.
    }
  }

  // ui 설정하는 함수.
  private void setupUi() {
    setContentView(R.layout.activity_main);
    initBtn();
    setBtnClickListener();
  }

  // Button 참조 변수 초기화하는 함수.
  private void initBtn() {
    btnPlay = findViewById(R.id.play);
    btnPause = findViewById(R.id.pause);
    btnRestart = findViewById(R.id.restart);
    btnStop = findViewById(R.id.stop);
    btnRecord = findViewById(R.id.record);
    btnRecordStop = findViewById(R.id.recordStop);
  }

  // Button 의 Click listener 설정하는 함수.
  private void setBtnClickListener() {
    btnPlay.setOnClickListener(view -> startPlaying());
    btnPause.setOnClickListener(view -> pausePlaying());
    btnRestart.setOnClickListener(view -> resumePlaying());
    btnStop.setOnClickListener(view -> stopPlaying());
    btnRecord.setOnClickListener(view -> recordAudio());
    btnRecordStop.setOnClickListener(view -> stopRecording());
  }

  // 녹음 파일 저장할 경로 지정하는 함수.
  private void setupStorage() {
    ContextWrapper cw = new ContextWrapper(getApplicationContext());
    File directory = cw.getDir("recordDir", Context.MODE_PRIVATE);
    File file = new File(directory, "recording.3gp");
    this.filePath = file.getAbsolutePath();
    Log.d("MainActivity_R", " - setupStorage" +
        "\ntarget path : " + filePath + "\n---");
  }

  // 녹음 [Listener].
  private void recordAudio() {
    setupRecorder();  // 녹음기 설정.
    try {
      recorder.prepare();
    } catch (IOException e) {
      Log.d("MainActivity_R", " - recordAudio" +
          "\n녹음 시작 안됨." + "\n---");
    }
    recorder.start();
  }

  // 녹음 중지 [Listener]
  private void stopRecording() {
    if (recorder != null) {
      recorder.stop();
      recorder.release();
      recorder = null;
    }
  }

  // 녹음기 설정하는 함수.
  private void setupRecorder() {
    recorder = new MediaRecorder();
    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);          // 어디에서 음성 데이터를 받을 것인지 정함.
    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  // 압축 형식 설정.
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);     // 인코딩 방식 설정.
    recorder.setOutputFile(filePath);
    Log.d("MainActivity_R", " - setupRecorder" +
        "\ntarget path : " + filePath + "\n---");
  }

  // 녹음본 재생 [Listener]
  private void startPlaying() {
    try {
      closePlayer();
      player = new MediaPlayer();
      player.setDataSource(filePath);
      player.prepare();
      player.start();
    } catch (IOException e) {
      Log.d("MainActivity_R", " - playAudio" +
          "\n녹음 파일 재생 안됨." + "\n---");
    }
  }

  // 녹음본 재생 중지 [Listener]
  private void pausePlaying() {
    if (player != null) {
      position = player.getCurrentPosition();
      player.pause();
    } else {
      Log.d("MainActivity_R", " - pauseAudio" +
          "\nplayer null. 녹음 파일 재생 중지 안됨." + "\n---");
    }
  }

  // 녹음본 재생 다시 시작 [Listener]
  private void resumePlaying() {
    if (player != null && !player.isPlaying()) {
      player.seekTo(position);
      player.start();
    } else {
      Log.d("MainActivity_R", " - resumeAudio" +
          "\n녹음 파일 다시 재생 안됨." + "\n---");
    }
  }

  // 녹음본 재생 아예 중지 [Listener]
  private void stopPlaying() {
    player.release();
    player = null;
  }

  // 재생기 닫는 함수.
  public void closePlayer() {
    if (player != null) {
      player.release();
      player = null;
    }
  }
}