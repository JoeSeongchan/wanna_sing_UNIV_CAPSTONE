package com.android.wannasing.home.fragment.myprofile.audio.recording.recyclerview;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wannasing.R;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter {

  //리사이클러뷰에 넣을 데이터 리스트
  List<Uri> dataModels;
  List<String> title;
  Context context;


  // 리스너 객체 참조를 저장하는 변수
  private OnItemClickListener listener = null;


  //생성자를 통하여 데이터 리스트
  public AudioAdapter(Context context, List<Uri> dataModels, List<String> title) {
    this.dataModels = dataModels;
    this.context = context;
    this.title = title;
  }

  // 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @Override
  public int getItemCount() {
    //데이터 리스트의 크기를 전달해주어야 함
    return dataModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_audio, parent, false);
    AudioViewHolder viewHolder = new AudioViewHolder(view);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    AudioViewHolder audioViewHolder = (AudioViewHolder) holder;

    String uriName = String.valueOf(title.get(position));

    audioViewHolder.audioTitle.setText(uriName);
  }


  // 1.커스텀 리스너 인터페이스 정의
  public interface OnItemClickListener {

    void onItemClick(View view, int position);
  }

  public class AudioViewHolder extends RecyclerView.ViewHolder {

    ImageButton audioBtn;
    TextView audioTitle;

    public AudioViewHolder(@NonNull View itemView) {
      super(itemView);
      audioBtn = itemView.findViewById(R.id.itemAudio_btn_play);
      audioTitle = itemView.findViewById(R.id.itemAudio_tv_title);

      audioBtn.setOnClickListener(view -> {
        //3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
          // 리스너 객체의 메서드 호출.
          if (listener != null) {
            listener.onItemClick(view, pos);
          }
        }
      });
    }
  }
}