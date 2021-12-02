package com.android.wannasing.home.fragment.partygroup.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.wannasing.db.entity.Party;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartyGroupInHomeViewModel extends ViewModel {

  private final FirebaseFirestore fireDb;
  private final int pageSize;
  private MutableLiveData<List<Party>> partyList;
  private DocumentSnapshot lastSeenDocSnap;

  public PartyGroupInHomeViewModel(FirebaseFirestore fireDb, int pageSize) {
    this.fireDb = fireDb;
    this.pageSize = pageSize;
  }

  public LiveData<List<Party>> getPartyList() {
    if (partyList == null) {
      partyList = new MutableLiveData<>();
      initPartyList();
    }
    return partyList;
  }

  public void retrieveNextPartyList() {
    Query query = fireDb.collection("party_list")
        .orderBy("meetingStartTime", Direction.ASCENDING)
        .startAfter(lastSeenDocSnap)
        .limit(pageSize);
    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
      lastSeenDocSnap = queryDocumentSnapshots.getDocuments()
          .get(queryDocumentSnapshots.size() - 1);
      List<Party> newlyAddedPartyList = queryDocumentSnapshots.toObjects(Party.class);
      List<Party> allPartyList = new ArrayList<>();
      // 기존 배열 추가.
      Optional.ofNullable(partyList.getValue())
          .ifPresent(allPartyList::addAll);
      // 새로 업데이트 받은 배열 추가.
      allPartyList.addAll(newlyAddedPartyList);
      // 배열 업데이트.
      partyList.postValue(allPartyList);
    });
  }

  private void initPartyList() {
    Query query = fireDb.collection("party_list")
        .orderBy("meetingStartTime", Direction.ASCENDING)
        .limit(pageSize);
    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
      lastSeenDocSnap = queryDocumentSnapshots.getDocuments()
          .get(queryDocumentSnapshots.size() - 1);
      List<Party> partyList = queryDocumentSnapshots.toObjects(Party.class);
      this.partyList.postValue(partyList);
    });
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    Log.d("PartyViewModel_R", "onCleared");
  }
}
