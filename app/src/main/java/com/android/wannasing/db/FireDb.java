package com.android.wannasing.db;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.wannasing.common.model.Entity;
import com.android.wannasing.db.FireDbException.Code;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.Transaction.Function;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.Optional;

public class FireDb<T extends Entity> extends ViewModel {

  private final FirebaseFirestore db;
  private final Class<T> dataClass;
  private final CollectionReference cltRef;
  private ListenerRegistration lisReg;
  private MutableLiveData<List<T>> dataList = null;

  public FireDb(String collectionPath, Class<T> dataClass) {
    this.dataClass = dataClass;
    db = FirebaseFirestore.getInstance();
    cltRef = db.collection(collectionPath);
  }

  public LiveData<List<T>> getDataList() {
    if (dataList == null) {
      dataList = new MutableLiveData<>();
      retrieveDataListFromFireDb();
    }
    return dataList;
  }

  // 실시간으로 업데이트 받는 함수.
  public void retrieveDataListFromFireDb() {
    this.lisReg = cltRef.addSnapshotListener((value, error) -> {
      if (error != null) {
        Utilities.log(LogType.w, "error : " + error.getMessage());
        return;
      }
      if (value == null) {
        Utilities.log(LogType.w, "error : value null.");
        return;
      }
      dataList.postValue(value.toObjects(dataClass));
    });
  }

  public Single<Optional<T>> getData(String primaryKey) {
    return Single.<Optional<T>>defer(() -> Single.create(
        emitter -> cltRef.document(primaryKey).get()
            .addOnSuccessListener(
                docSnap -> emitter.onSuccess(Optional.ofNullable(docSnap.toObject(dataClass))))
            .addOnFailureListener(
                err -> emitter.onError(new FireDbException("data not existed,", Code.NOT_FOUND)))))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io());
  }

  // DB 에 정보가 존재함을 확인하는 함수.
  public boolean checkDataExisted(T data, Transaction transaction) {
    try {
      return transaction.get(cltRef.document(data.getPrimaryKey())).exists();
    } catch (FirebaseFirestoreException e) {
      Utilities.log(LogType.w, "error : " + e.getMessage());
      return false;
    }
  }

  // DB 에 데이터 추가/수정하는 함수.
  public void setData(T data, Transaction transaction) {
    transaction.set(cltRef.document(data.getPrimaryKey()), data);
  }

  // DB 에서 데이터 삭제하는 함수.
  public void deleteData(T data, Transaction transaction) {
    transaction.delete(cltRef.document(data.getPrimaryKey()));
  }

  public void deleteData(String primaryKey, Transaction transaction) {
    transaction.delete(cltRef.document(primaryKey));
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    turnOffListener();
  }

  public void turnOffListener() {
    if (this.lisReg != null) {
      this.lisReg.remove();
    }
  }

  public static class TransactionManager {

    final FirebaseFirestore db;

    public TransactionManager() {
      this.db = FirebaseFirestore.getInstance();
    }

    public void run(Function<Void> updateFunction) {
      db.runTransaction(updateFunction);
    }
  }
}
