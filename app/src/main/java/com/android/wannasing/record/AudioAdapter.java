package com.android.wannasing.record;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.wannasing.R;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter {
    //리사이클러뷰에 넣을 데이터 리스트
    ArrayList<Uri> dataModels;
    ArrayList<String> title;
    Context context;


    // 리스너 객체 참조를 저장하는 변수
    private OnIconClickListener listener = null;


    // 1.커스텀 리스너 인터페이스 정의
    public interface OnIconClickListener {
        void onItemClick(View view, int position);
    }

    // 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
    public void setOnItemClickListener(OnIconClickListener listener) {
        this.listener = listener;
    }

    //생성자를 통하여 데이터 리스트
    public AudioAdapter(Context context, ArrayList<Uri> dataModels, ArrayList<String> title) {
        this.dataModels = dataModels;
        this.context = context;
        this.title = title;
    }

    @Override
    public int getItemCount() {
        //데이터 리스트의 크기를 전달해주어야 함
        return dataModels.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder)holder;

        String uriName = String.valueOf(title.get(position));

        myViewHolder.audioTitle.setText(uriName);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton audioBtn;
        TextView audioTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audioBtn = itemView.findViewById(R.id.playBtn_itemAudio);
            audioTitle = itemView.findViewById(R.id.audioTitle_itemAudio);

            audioBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (listener != null) {
                            listener.onItemClick(view, pos) ;
                        }
                    }
                }
            });
        }
    }
}