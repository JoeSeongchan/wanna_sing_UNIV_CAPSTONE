package com.android.wannasing.feature.party.showpartygroup.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import java.util.List;

public class PartyGroupInHomeViewModel extends ViewModel {

  private final FirebaseFirestore fireDb;
  private MutableLiveData<List<Party>> partyList;

  public PartyGroupInHomeViewModel(FirebaseFirestore fireDb) {
    this.fireDb = fireDb;
  }

  public LiveData<List<Party>> getPartyList() {
    if (partyList == null) {
      partyList = new MutableLiveData<>();
      initPartyList();
    }
    return partyList;
  }

  private void initPartyList() {
    Query query = fireDb.collection("party_list")
        .orderBy("meetingStartTime", Direction.ASCENDING);
    query.addSnapshotListener((value, error) -> {
      if (value == null) {
        Utilities.log(LogType.w, "FAIL : value is null.");
        return;
      } else if (error != null) {
        Utilities.log(LogType.w, "FAIL : " + error.getMessage());
        return;
      } else {
        List<Party> partyList = value.toObjects(Party.class);
        this.partyList.postValue(partyList);
      }
    });
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    Log.d("PartyViewModel_R", "onCleared");
  }
}
