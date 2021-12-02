package com.android.wannasing.feature.profile.showmyprofile.viewcontroller;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import com.android.wannasing.common.model.User;
import com.android.wannasing.feature.audio.recordaudio.viewcontroller.AudioAdapter;
import com.android.wannasing.feature.audio.uploadaudio.viewcontroller.SetUploadOptionActivity;
import com.android.wannasing.feature.profile.editprofile.viewcontroller.EditProfileActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ShowMyProfileFragment extends Fragment {

  public static final String ARGS_USER_INFO = "USER_INFO";
  //로그인 된 유저 id
  public String id = "IOxZt7qFPxdE93PNtElzSztmYpP2";
  // 데이터베이스
  protected FirebaseFirestore db = FirebaseFirestore.getInstance();
  protected FirebaseStorage storage = FirebaseStorage.getInstance();
  ImageView playIcon;
  private User currentUser;
  private TextView username, userid, profile, evaluate, eval1, eval2, eval3, eval4;
  private ImageButton add, option;

  private View rootView;
  /**
   * 오디오 파일 관련 변수
   */
  // 오디오 파일 녹음 관련 변수
  private Uri audioUri = null; // 오디오 파일 uri
  // 오디오 파일 재생 관련 변수
  private MediaPlayer mediaPlayer = null;
  private Boolean isPlaying = false;
  /**
   * 리사이클러뷰
   */
  private AudioAdapter audioAdapter;
  private ArrayList<Uri> audioList;
  //노래 리스트
  private RecyclerView audioRecyclerView;
  //나이 계산 관련
  private int result = 3;
  private List<String> titleList;

  public ShowMyProfileFragment() {
  }

  // 인스턴스 만드는 함수. (팩토리 메소드)
  public static ShowMyProfileFragment newInstance(User user) {
    ShowMyProfileFragment instance = new ShowMyProfileFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARGS_USER_INFO, user);
    instance.setArguments(args);
    return instance;
  }

  // 인자 받는 함수.
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Optional<Bundle> arg = Optional.ofNullable(getArguments());
    this.currentUser = arg.map(bundle -> (User) bundle.getSerializable("USER_INFO"))
        .orElse(User.DUMMY_USER);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_show_my_profile, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.rootView = view;
    // get song data from firebase storage, findviewbyid all button
    retrieveSongDataFromFireStorage();
    showSongDataThroughUi();  //get extradata from firestore and show
    addClickListener();        //add click listener
  }

  private void initViewComponentInstance() {
    username = rootView.findViewById(R.id.username);
    userid = rootView.findViewById(R.id.userid);
    profile = rootView.findViewById(R.id.profile);
    evaluate = rootView.findViewById(R.id.evaluate);
    eval1 = rootView.findViewById(R.id.eval1c);
    eval2 = rootView.findViewById(R.id.eval2c);
    eval3 = rootView.findViewById(R.id.eval3c);
    eval4 = rootView.findViewById(R.id.eval4c);
    add = rootView.findViewById(R.id.add);
    option = rootView.findViewById(R.id.option);

    // 리사이클러뷰
    audioRecyclerView = rootView.findViewById(R.id.showMyProfile_rv_songList);
    audioList = new ArrayList<>();
    titleList = new ArrayList<>();
    audioAdapter = new AudioAdapter(getActivity().getApplicationContext(), audioList, titleList);
  }

  private void retrieveSongDataFromFireStorage() {
    initViewComponentInstance();

    StorageReference listRef = storage.getReference().child(id);

    listRef.listAll()
        .addOnSuccessListener(listResult -> {
          for (StorageReference prefix : listResult.getPrefixes()) {
            Log.d("wow", audioUri.toString());
            Log.d("upload", prefix.toString());
          }

          for (StorageReference item : listResult.getItems()) {
            item.getDownloadUrl().addOnSuccessListener(uri -> {
              audioList.add(uri);
              titleList.add(item.getName());
              audioAdapter.notifyDataSetChanged();
              Log.d("upload", uri.toString());
            });
          }
        })
        .addOnFailureListener(e -> Log.d("upload", e.getMessage()));

    // 이주엽 수정부분 22222222222222 리사이클러뷰 보여주기
    audioRecyclerView.setAdapter(audioAdapter);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    audioRecyclerView.setLayoutManager(mLayoutManager);

    // 커스텀 이벤트 리스너 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달
    audioAdapter.setOnItemClickListener((view, position) -> {

      String file = String.valueOf(audioList.get(position));

              /*음성 녹화 파일에 대한 접근 변수 생성;
                   (ImageView)를 붙여줘서 View 객체를 형변환 시켜줌.
                   전역변수로 한 이유는
                  * */

      if (isPlaying) {
        // 음성 녹화 파일이 여러개를 클릭했을 때 재생중인 파일의 Icon을 비활성화(비 재생중)으로 바꾸기 위함.
        if (playIcon == (ImageView) view) {
          // 같은 파일을 클릭했을 경우
          stopAudio();
        } else {
          // 다른 음성 파일을 클릭했을 경우
          // 기존의 재생중인 파일 중지
          stopAudio();
          // 새로 파일 재생하기
          playIcon = (ImageView) view;
          playAudio(file);
        }
      } else {
        playIcon = (ImageView) view;
        playAudio(file);
      }
    });
  }

  private void showSongDataThroughUi() {
    db.collection("user_list")
        .document(id)
        .get()
        .addOnCompleteListener(task2 -> {
          if (task2.isSuccessful()) {
            DocumentSnapshot document = task2.getResult();
            Date birthDate = Optional.ofNullable((Timestamp) document
                .get("dateOfBirth"))
                .map(Timestamp::toDate)
                .orElse(new Date());
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            result = Integer.parseInt(df.format(birthDate));
            int nowtime = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            result = nowtime - result + 1;
          } else {
            Log.d(TAG, "YOU MUST LOGIN FIRST");
          }
        });

    db.collection("user_profile")
        .document(id)
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            Log.d(TAG, document.getId() + " => " + document.getData());
            username.setText(document.getData().get("nickname").toString());
            userid.setText(document.getData().get("name").toString());
            profile.setText(
                "\n나이: " + result + "\n성별: " + document.getData().get("gender")
                    .toString() +
                    "\n활동지역: " + document.getData().get("primelocal").toString() + "\n선호하는 장르: "
                    + document.getData().get("primegenre").toString());
            eval1.setText(document.getData().get("evaluation1").toString());
            eval2.setText(document.getData().get("evaluation2").toString());
            eval3.setText(document.getData().get("evaluation3").toString());
            eval4.setText(document.getData().get("evaluation4").toString());
          } else {
            Log.d(TAG, "YOU MUST LOGIN FIRST");
            android.os.Process.killProcess(android.os.Process.myPid());
          }
        });
  }

  private void addClickListener() {
    add.setOnClickListener(view -> {
      Intent intent = new Intent(getActivity().getApplicationContext(),
          SetUploadOptionActivity.class);
      startActivity(intent);
    });

    option.setOnClickListener(view -> {
      Intent intent = new Intent(getActivity().getApplicationContext(),
          EditProfileActivity.class);
      startActivity(intent);
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

    playIcon.setImageDrawable(

        ResourcesCompat
            .getDrawable(getResources(), R.drawable.audio_pause_icon, null));
    isPlaying = true;
    mediaPlayer.setOnCompletionListener(mp -> stopAudio());
  }

  // 녹음 파일 중지
  private void stopAudio() {
    playIcon.setImageDrawable(
        ResourcesCompat
            .getDrawable(getResources(), R.drawable.audio_play_icon, null));
    isPlaying = false;
    mediaPlayer.stop();
  }
}
