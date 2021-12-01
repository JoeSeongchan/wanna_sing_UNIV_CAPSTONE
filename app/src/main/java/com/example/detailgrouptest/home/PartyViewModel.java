package com.example.detailgrouptest.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.detailgrouptest.db.entity.Party;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.List;

public class PartyViewModel extends ViewModel {

  private MutableLiveData<List<Party>> partyList;
  private FirebaseFirestore fireDb;

  public PartyViewModel(FirebaseFirestore fireDb) {
    this.fireDb = fireDb;
  }

  public LiveData<List<Party>> getPartyList() {
    if (partyList == null) {
      partyList = new MutableLiveData<>();
      loadPartyList();
    }
    return partyList;
  }

  public void getNextPartyList(DocumentSnapshot lastSeenDocSnap) {
    Query query = fireDb.collection("party_list")
        .orderBy("meetingStartTime")
        .startAfter(lastSeenDocSnap);
    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
      List<Party> newlyAddedPartyList = queryDocumentSnapshots.toObjects(Party.class);
      List<>
      newPartyList.addAll(partyList.getValue());
      partyList.getValue().add
      partyList.setValue(newPartyList)
    })
  }

  private void loadPartyList() {
    // Do an asynchronous operation to fetch users.
  }
}
