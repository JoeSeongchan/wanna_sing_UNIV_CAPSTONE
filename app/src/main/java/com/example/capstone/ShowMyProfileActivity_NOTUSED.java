package com.example.capstone;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class ShowMyProfileActivity_NOTUSED extends AppCompatActivity {
    private TextView username, userid, mainsinger, song, profile, profilename, evaluate, eval1, eval2, eval3, eval4, favoritesongs;
    private ImageButton add, option;

    /**xml 변수*/
    ImageButton audioRecordImageBtn;
    TextView audioRecordText;

    /**오디오 파일 관련 변수*/

    // 오디오 파일 녹음 관련 변수
    private Uri audioUri = null; // 오디오 파일 uri

    // 이주엽 수정부분 111111111111111111
    private File audiofilespath =new File("data/user/0/com.example.capstone/app_recordDir");
    private File[] audiofiles = audiofilespath.listFiles();

    // 오디오 파일 재생 관련 변수
    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    ImageView playIcon;

    /** 리사이클러뷰 */
    private AudioAdapter audioAdapter;
    private ArrayList<Uri> audioList;

    //노래 리스트
    private RecyclerView recyclerView;

    //나이 계산 관련
    private int result=3;

    //로그인 된 유저 id
    protected String id = "IOxZt7qFPxdE93PNtElzSztmYpP2";


    // 데이터베이스
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected FirebaseStorage storage = FirebaseStorage.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        permissionCheck();
        init();
        Toast.makeText(this, "init 완료", Toast.LENGTH_SHORT).show();
        showprofile();
        click();
        changeprofile();
    }

    private void changeprofile() {

    }

    private void init() {
        username = findViewById(R.id.username);
        userid = findViewById(R.id.userid);
        profile = findViewById(R.id.profile);
        recyclerView = findViewById(R.id.recycler_songs);
        evaluate = findViewById(R.id.evaluate);
        eval1 = findViewById(R.id.eval1c);
        eval2 = findViewById(R.id.eval2c);
        eval3 = findViewById(R.id.eval3c);
        eval4 = findViewById(R.id.eval4c);
        add = findViewById(R.id.add);
        option = findViewById(R.id.option);


        // 리사이클러뷰
        RecyclerView audioRecyclerView = findViewById(R.id.recycler_songs);
        audioList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();
        audioAdapter = new AudioAdapter(this, audioList,titleList);


        StorageReference listRef = storage.getReference().child(id);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {

                            Log.d("wow", audioUri.toString());
                            Log.d("upload", prefix.toString());
                        }

                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    audioList.add(uri);
                                    titleList.add(item.getName());
                                    audioAdapter.notifyDataSetChanged();
                                    Log.d("upload", uri.toString());
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("upload", e.getMessage());
                    }
                });

        // 이주엽 수정부분 22222222222222 리사이클러뷰 보여주기

        audioRecyclerView.setAdapter(audioAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);

        // 커스텀 이벤트 리스너 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnIconClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String file = String.valueOf(audioList.get(position));

                /*음성 녹화 파일에 대한 접근 변수 생성;
                     (ImageView)를 붙여줘서 View 객체를 형변환 시켜줌.
                     전역변수로 한 이유는
                    * */



                if(isPlaying){
                    // 음성 녹화 파일이 여러개를 클릭했을 때 재생중인 파일의 Icon을 비활성화(비 재생중)으로 바꾸기 위함.
                    if(playIcon == (ImageView)view){
                        // 같은 파일을 클릭했을 경우
                        stopAudio();
                    } else {
                        // 다른 음성 파일을 클릭했을 경우
                        // 기존의 재생중인 파일 중지
                        stopAudio();
                        // 새로 파일 재생하기
                        playIcon = (ImageView)view;
                        playAudio(file);
                    }
                } else {
                    playIcon = (ImageView)view;
                    playAudio(file);
                }
            }
        });
    }

    private void showprofile() {
        db.collection("user_list")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                        if (task2.isSuccessful()) {
                            DocumentSnapshot document = task2.getResult();
                            Date birthDate = Optional.ofNullable((Timestamp) document
                                    .get("dateOfBirth"))
                                    .map(Timestamp::toDate)
                                    .orElse(new Date());
                            SimpleDateFormat df = new SimpleDateFormat("yyyy");
                            result = Integer.parseInt(df.format(birthDate));
                            int nowtime = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
                            result =nowtime-result+1;
                        } else {
                            Log.d(TAG, "YOU MUST LOGIN FIRST");
                        }
                    }
                });

        db.collection("user_profile")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            username.setText(document.getData().get("nickname").toString());
                            userid.setText(document.getData().get("name").toString());
                            profile.setText("\n나이: " + Integer.toString(result) + "\n성별: " + document.getData().get("gender").toString() +
                                    "\n활동지역: " + document.getData().get("primelocal").toString() + "\n선호하는 장르: " + document.getData().get("primegenre").toString());
                            eval1.setText(document.getData().get("evaluation1").toString());
                            eval2.setText(document.getData().get("evaluation2").toString());
                            eval3.setText(document.getData().get("evaluation3").toString());
                            eval4.setText(document.getData().get("evaluation4").toString());
                        } else {
                            Log.d(TAG, "YOU MUST LOGIN FIRST");
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                });
    }

    private void click() {
        add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectUploadOptionActivity.class);
                startActivity(intent);
            }
        });

        option.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileChangeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void playAudio(String file) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_pause, null));
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
            }
        });

    }

    // 녹음 파일 중지
    private void stopAudio() {
        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_play, null));
        isPlaying = false;
        mediaPlayer.stop();
    }

    public void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }


}
