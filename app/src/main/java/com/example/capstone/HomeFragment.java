package com.example.capstone;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.database.GroupDisplay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {


    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ShowMyProfileActivity_NOTUSED myprofile;

    //리사이클러 뷰 관련
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private DocumentSnapshot lastVisible;
    private int limit=5;

    // 여기서부터는 데이터베이스
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    /** 리사이클러뷰 */
    private GroupAdapter groupAdapter;
    private List<GroupDisplay> groupList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Context context=container.getContext();
        recyclerv(v);
        Log.d("TEST", "Recyclerv");
        initrest(v);
        Log.d("TEST", "initrest");
        return v;
    }


    private void recyclerv(View v) {
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(getActivity(), groupList);
        RecyclerView grouprecyclerView = v.findViewById(R.id.recycler_view);

        grouprecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        grouprecyclerView.setAdapter(groupAdapter);


        long nowtime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
        Log.d("TEST", String.valueOf(nowtime));
        db.collection("group")
                .limit(limit)
                .whereGreaterThan("starttime", nowtime)
                .whereLessThan("starttime", nowtime + 030000)
                .orderBy("starttime", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() <= 0) {
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TEST", document.getId() + " => " + document.getData());
                                    groupList.add(new GroupDisplay(
                                            document.getData().get("title").toString(),
                                            "서울시",
                                            3,
                                            2,
                                            Long.parseLong(document.getData().get("starttime").toString())));
                                }
                                groupAdapter.notifyDataSetChanged();
                                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                //스크롤 리스터
                                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView grouprecyclerview, int newState) {
                                        super.onScrollStateChanged(grouprecyclerview, newState);
                                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                            isScrolling = true;
                                        }
                                    }

                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                        int visibleItemCount = linearLayoutManager.getChildCount();
                                        int totalItemCount = linearLayoutManager.getItemCount();
                                        if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                            isScrolling = false; // 필드명 수정
                                            Query nextQuery = db.collection("group")
                                                    .orderBy("starttime")
                                                    .startAfter(lastVisible)
                                                    .limit(limit);
                                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                    if (t.getResult().size() > 0) {
                                                        if (t.isSuccessful()) {
                                                            for (DocumentSnapshot d : t.getResult()) {
                                                                // 데이터 모델 수정
                                                                groupList.add(new GroupDisplay(
                                                                        d.getData().get("title").toString(),
                                                                        "서울시",
                                                                        3,
                                                                        2,
                                                                        Long.parseLong(d.getData().get("starttime").toString())));
                                                            }
                                                            // 어댑터 수정
                                                            groupAdapter.notifyDataSetChanged();
                                                            lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                                            if (t.getResult().size() < limit) {
                                                                isLastItemReached = true;
                                                            }
                                                        }
                                                    }

                                                }
                                            });
                                        }
                                    }
                                };
                                grouprecyclerView.addOnScrollListener(onScrollListener);
                            }
                        }
                        else{Log.d("TEST", "안되는데요?");}
                    }
                });

    }

    private void initrest(View v){
        v.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerv(v);
                Toast.makeText(getActivity(),"눌렸다.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
