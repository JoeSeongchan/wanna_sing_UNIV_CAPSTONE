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

  private void initChatGroupList() {
    Observable.<List<Joins>>defer(
        () -> Observable.create(emitter -> fireDb.collection(JOINS_COLLECTION_PATH)
            .whereEqualTo("memberId", user.getId())
            .addSnapshotListener((value, error) -> {
              if (error != null) {
                emitter.onError(new Throwable(Utilities.makeLog("err.")));
              } else if (!value.isEmpty()) {
                List<Joins> joinsList = value.toObjects(Joins.class);
                emitter.onNext(joinsList);
              }
            })))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .flatMap(Observable::fromIterable)
        .flatMap(joins -> Observable.<Party>defer(
            () -> Observable.create(emitter -> fireDb.collection(PARTY_COLLECTION_PATH)
                .whereEqualTo("partyName", joins.partyName)
                .whereEqualTo("hostId", joins.hostId)
                .addSnapshotListener((value, error) -> {
                  if (error != null) {
                    emitter.onError(new Throwable(Utilities.makeLog("err.")));
                  } else if (!value.isEmpty()) {
                    List<Party> partyList = value.toObjects(Party.class);
                    emitter.onNext(partyList.get(0));
                  }
                }))))
        .map(ChatGroup::new)
        .subscribe(chatGroup -> {
          Utilities.log(LogType.d, "chatGroup : " + chatGroup.getName());
          List<ChatGroup> fixedChatGroupList = chatGroupList.getValue();
          fixedChatGroupList.remove(chatGroup);
          fixedChatGroupList.add(chatGroup);
          chatGroupList.postValue(fixedChatGroupList);
        }, err -> Utilities.log(LogType.w, "err : " + err.getMessage()));
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    Log.d("PartyViewModel_R", "onCleared");
  }
}
