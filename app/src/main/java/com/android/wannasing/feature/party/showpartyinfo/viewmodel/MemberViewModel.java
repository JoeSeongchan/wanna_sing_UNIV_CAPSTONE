package com.android.wannasing.feature.party.showpartyinfo.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.wannasing.common.model.Joins;
import com.android.wannasing.common.viewcontroller.FireDb;
import com.android.wannasing.common.viewcontroller.FireDb.TransactionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.List;
import java.util.stream.Collectors;

public class MemberViewModel extends ViewModel {

  public static final String TAG = "MemberViewModel_R";
  private final FirebaseFirestore fireDb;
  private final String hostId;
  private final String partyName;
  private final FireDb<Joins> joinsFireDb;
  private final TransactionManager transactionManager;
  private MutableLiveData<List<String>> memberIdList;

  public MemberViewModel(FirebaseFirestore fireDb, String hostId, String partyName) {
    this.fireDb = fireDb;
    this.hostId = hostId;
    this.partyName = partyName;
    this.joinsFireDb = new FireDb<>("joins_list", Joins.class);
    this.transactionManager = new TransactionManager();
  }

  public LiveData<List<String>> getMemberIdList() {
    if (memberIdList == null) {
      memberIdList = new MutableLiveData<>();
      initMemberList();
    }
    return memberIdList;
  }

  private void initMemberList() {
    // joins list 에서 해당 모임에 참가 중인 멤버 리스트를 찾는다.
    Query query = fireDb.collection("joins_list")
        .whereEqualTo("hostId", hostId)
        .whereEqualTo("partyName", partyName);
    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
      List<Joins> joinsList = queryDocumentSnapshots.toObjects(Joins.class);
      // 멤버 id 리스트로 변환한다.
      List<String> memberIdList = joinsList.stream().map(joins -> joins.memberId)
          .collect(Collectors.toList());
      memberIdList.sort((s1, s2) -> {
        if (s1.equals(hostId)) {
          return -1;
        } else if (s2.equals(hostId)) {
          return 1;
        }
        return 0;
      });
      this.memberIdList.postValue(memberIdList);
    });
  }

  public void makeJoins(String memberId) {
    transactionManager.run(transaction -> {
      Joins newJoins = new Joins(memberId, hostId, partyName);
      joinsFireDb.setData(newJoins, transaction);
      return null;
    });
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    Log.d(TAG, "onCleared");
  }
}
