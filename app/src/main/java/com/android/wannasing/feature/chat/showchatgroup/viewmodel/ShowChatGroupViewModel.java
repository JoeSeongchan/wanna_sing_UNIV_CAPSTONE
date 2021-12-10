package com.android.wannasing.feature.chat.showchatgroup.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.wannasing.common.model.Joins;
import com.android.wannasing.common.model.User;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.feature.party.common.model.Party;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.firestore.FirebaseFirestore;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class ShowChatGroupViewModel extends ViewModel {

  public static final String JOINS_COLLECTION_PATH = "joins_list";
  public static final String PARTY_COLLECTION_PATH = "party_list";
  private final FirebaseFirestore fireDb;
  private final User user;
  private MutableLiveData<List<ChatGroup>> chatGroupList;

  public ShowChatGroupViewModel(FirebaseFirestore fireDb, User user) {
    this.fireDb = fireDb;
    this.user = user;
  }

  public LiveData<List<ChatGroup>> getChatGroupList() {
    if (chatGroupList == null) {
      chatGroupList = new MutableLiveData<>();
      chatGroupList.setValue(new ArrayList<>());
      initChatGroupList();
    }
    return chatGroupList;
  }

  public void initChatGroupList() {
    Single.<List<Joins>>defer(
        () -> Single.create(emitter -> fireDb.collection(JOINS_COLLECTION_PATH)
            .whereEqualTo("memberId", user.getId())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> emitter
                .onSuccess(queryDocumentSnapshots.toObjects(Joins.class)))
            .addOnFailureListener(emitter::onError)))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .flatMapObservable(Observable::fromIterable)
        .flatMapSingle(joins -> Single.<Party>defer(
            () -> Single.create(emitter -> fireDb.collection(PARTY_COLLECTION_PATH)
                .whereEqualTo("partyName", joins.partyName)
                .whereEqualTo("hostId", joins.hostId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> emitter
                    .onSuccess(queryDocumentSnapshots.toObjects(Party.class).get(0)))
                .addOnFailureListener(emitter::onError))))
        .map(ChatGroup::new)
        .subscribe(chatGroup -> {
//          Utilities.log(LogType.d, "chatGroup : " + chatGroup.getName());
          List<ChatGroup> fixedChatGroupList = chatGroupList.getValue();
          fixedChatGroupList.remove(chatGroup);
          fixedChatGroupList.add(chatGroup);
          chatGroupList.setValue(fixedChatGroupList);
        }, err -> Utilities.log(LogType.w, "err : " + err.getMessage()));
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    Log.d("PartyViewModel_R", "onCleared");
  }
}
