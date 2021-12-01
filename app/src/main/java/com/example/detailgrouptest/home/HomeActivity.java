package com.example.detailgrouptest.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.capstone.database.groupdisplay;
import com.example.detailgrouptest.R;
import com.example.detailgrouptest.databinding.ActivityHomeBinding;
import com.example.detailgrouptest.db.entity.Party;
import com.example.detailgrouptest.home.recyclerview.adapter.HomePartyAdapter;
import com.example.detailgrouptest.home.recyclerview.adapter.HomePartyAdapter.HomePartyDiff;
import com.example.detailgrouptest.home.recyclerview.viewholder.HomePartyViewHolder.OnPartyItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements OnPartyItemClickListener {

  private static final int ITEM_SHOW_LIMIT = 5;
  // 여기서부터는 데이터베이스
  protected FirebaseFirestore db = FirebaseFirestore.getInstance();
  BottomNavigationView bottomNavigationView;
  private BottomNavigationView bottomNav; // 바텀 네비게이션 뷰
  private FragmentManager fm;
  private FragmentTransaction ft;
  //리사이클러 뷰 관련
  private boolean isScrolling = false;
  private boolean isLastItemReached = false;
  private DocumentSnapshot lastVisible;
  private HomePartyAdapter adapter;
  private ActivityHomeBinding binding;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityHomeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    recyclerv();
    initrest();
  }


  private void recyclerv() {
    adapter = new HomePartyAdapter(new HomePartyDiff(), this);
    RecyclerView rv = binding.mainRvPartyList;
    rv.setLayoutManager(new LinearLayoutManager(this));
    rv.setAdapter(adapter);

    long nowtime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
    db.collection("group")
        .limit(ITEM_SHOW_LIMIT)
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
                  Log.d(TAG, document.getId() + " => " + document.getData());
                  groupList.add(new groupdisplay(
                      document.getData().get("title").toString(),
                      "서울시",
                      3,
                      2,
                      Long.parseLong(document.getData().get("starttime").toString())));
                }
                adapter.notifyDataSetChanged();
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
                    LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView
                        .getLayoutManager());
                    int firstVisibleItemPosition = linearLayoutManager
                        .findFirstVisibleItemPosition();
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    if (isScrolling && (firstVisibleItemPosition + visibleItemCount
                        == totalItemCount) && !isLastItemReached) {
                      isScrolling = false; // 필드명 수정
                      Query nextQuery = db.collection("group")
                          .orderBy("starttime")
                          .startAfter(lastVisible)
                          .limit(ITEM_SHOW_LIMIT);
                      nextQuery.get()
                          .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                              if (t.getResult().size() > 0) {
                                if (t.isSuccessful()) {
                                  for (DocumentSnapshot d : t.getResult()) {
                                    // 데이터 모델 수정
                                    groupList.add(new groupdisplay(
                                        d.getData().get("title").toString(),
                                        "서울시",
                                        3,
                                        2,
                                        Long.parseLong(d.getData().get("starttime").toString())));

                                  }
                                  // 어댑터 수정
                                  adapter.notifyDataSetChanged();
                                  lastVisible = t.getResult().getDocuments()
                                      .get(t.getResult().size() - 1);
                                  if (t.getResult().size() < ITEM_SHOW_LIMIT) {
                                    isLastItemReached = true;
                                  }
                                }
                              }

                            }
                          });
                    }
                  }
                };
                rv.addOnScrollListener(onScrollListener);
              }
            }
          }
        });
  }

  private void initrest() {
    findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recyclerv();
      }
    });
  }

  @Override
  public void onPartyItemClick(Party party) {
    Toast.makeText(this,
        String.format("party item clicked : %1$s", party.partyName), Toast.LENGTH_SHORT).show();
  }
}
