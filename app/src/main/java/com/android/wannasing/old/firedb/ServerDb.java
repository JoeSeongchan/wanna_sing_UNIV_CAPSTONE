package com.android.wannasing.old.firedb;


import com.android.wannasing.feature.chat.showchat.model.Data;
import com.android.wannasing.old.firedb.change.DbChanges;
import com.android.wannasing.old.firedb.change.DbChanges.DbChangesType;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;


public class ServerDb<T extends Data> {

  protected FirebaseFirestore db;
  protected Class<T> dataClass;
  protected CollectionReference cltRef;
  protected ListenerRegistration lisReg;

  public ServerDb(String fullPath, Class<T> dataClass) {
    this.dataClass = dataClass;
    db = FirebaseFirestore.getInstance();
    cltRef = db.collection(fullPath);
  }

  // 실시간으로 업데이트 받는 함수.
  public Observable<DbChanges<T>> getUpdateInRealTime() {
    // 변경 리스트 받아오기.
    return Observable.<List<DocumentChange>>defer(() -> Observable.create(emitter -> {
      this.lisReg = cltRef.addSnapshotListener((value, error) -> {
        if (error != null) {
          Utilities.log(LogType.w, "db realtime method error not null.");
          emitter.onError(new Throwable(Utilities.makeLog("db realtime method error.")));
          return;
        }
        if (value == null) {
          Utilities.log(LogType.w, "db realtime method value null.");
          emitter.onError(new Throwable(Utilities.makeLog("db realtime method value null.")));
          return;
        }
        emitter.onNext(value.getDocumentChanges());
      });
    }))
        // 여기서 스레드 끊어준다.
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        // 계산 스레드 사용.
        .flatMap(Observable::fromIterable)
        // DbChange 로 변경해서 발행.
        .map(docChange -> {
          T changes = dataClass.cast(docChange.getDocument()
              .toObject(dataClass));
          switch (docChange.getType()) {
            case ADDED:
              Utilities.log(LogType.d, "add item.");
              return new DbChanges<>(changes, DbChangesType.ADD);
            case MODIFIED:
              Utilities.log(LogType.d, "modify item.");
              return new DbChanges<>(changes, DbChangesType.MODIFY);
            case REMOVED:
              Utilities.log(LogType.d, "delete item.");
              return new DbChanges<>(changes, DbChangesType.DELETE);
            default:
              throw new Throwable(Utilities.makeLog("no type."));
          }
        });
  }

  // 모든 아이템을 Firestore 서버에서 받아오는 함수.
  public Single<List<T>> getAllItem() {
    return Single
        .defer(() -> Single.create(emitter ->
            cltRef.get().addOnSuccessListener(querySnapshot -> {
              List<T> items = querySnapshot.toObjects(dataClass);
              emitter.onSuccess(items);
            }).addOnFailureListener(e ->
                emitter.onError(new Throwable(Utilities.makeLog(e.getMessage()))))));
  }

  // DB 에 정보가 존재함을 확인하는 함수.
  public boolean checkDataExisted(T data, Transaction transaction)
      throws FirebaseFirestoreException {
    return transaction.get(cltRef.document(data.getPrimaryKey())).exists();
  }

  public boolean checkDataEqual(T data, Transaction transaction)
      throws FirebaseFirestoreException {
    return transaction.get(cltRef.document(data.getPrimaryKey())).toObject(dataClass)
        .equals(data);
  }

  // DB 에 정보가 존재함을 확인하는 함수.
  public Completable checkDataExisted(T data) {
    if (cltRef == null) {
      return Completable.error(new Throwable(Utilities.makeLog("cltRef null.")));
    }
    return Completable.create(
        emitter -> cltRef.document(data.getPrimaryKey()).get().addOnCompleteListener(task -> {
          DocumentSnapshot docSnap = task.getResult();
          if (docSnap == null) {
            emitter.onError(new Throwable(Utilities.makeLog("transmission error.")));
            return;
          } else if (!docSnap.exists()) {
            emitter
                .onError(new Throwable(Utilities.makeLog("check the existence of data. [FAIL]")));
            return;
          }
          Utilities.log(LogType.d, "check the existence of data. [SUCCESS]" +
              "\nprimary key of data : " + data.getPrimaryKey());
          emitter.onComplete();
        }));
  }

  // DB 에 정보가 존재하지 않음을 확인하는 함수.
  public Completable checkDataNotExisted(T data) {
    if (cltRef == null) {
      return Completable.error(new Throwable(Utilities.makeLog("cltRef null.")));
    }
    return Completable.create(emitter -> {
      cltRef.document(data.getPrimaryKey()).get().addOnCompleteListener(task -> {
        DocumentSnapshot docSnap = task.getResult();
        if (docSnap == null) {
          emitter.onError(new Throwable(Utilities.makeLog("transmission error.")));
          return;
        } else if (docSnap.exists()) {
          emitter.onError(
              new Throwable(Utilities.makeLog("check that the data does not exist. [FAIL]")));
          return;
        }
        Utilities.log(LogType.d, "check that the data does not exist. [SUCCESS]" +
            "\nprimary key of data : " + data.getPrimaryKey());
        emitter.onComplete();
      });
    });
  }

  public Completable checkDataEqual(T data) {
    if (cltRef == null) {
      return Completable.error(new Throwable(Utilities.makeLog("cltRef null.")));
    }
    return Completable.create(
        emitter -> cltRef.document(data.getPrimaryKey()).get().addOnCompleteListener(task -> {
          DocumentSnapshot docSnap = task.getResult();
          if (docSnap == null) {
            emitter.onError(new Throwable(Utilities.makeLog("transmission error.")));
            return;
          } else if (!docSnap.exists()) {
            emitter.onError(new Throwable(Utilities.makeLog("data not existed.")));
            return;
          }
          T dataInDb = docSnap.toObject(dataClass);
          // data 동일 여부 체크.
          if (dataInDb.equals(data)) {
            Utilities.log(LogType.d, "data equal verification [SUCCESS]" +
                "\nprimary key of data : " + data.getPrimaryKey());
            emitter.onComplete();
          } else {
            emitter.onError(new Throwable(Utilities.makeLog("data equal verification [FAIL]" +
                "\nprimary key of data : " + data.getPrimaryKey())));
          }
        }));
  }

  public Completable checkDataNotEqual(T data) {
    if (cltRef == null) {
      return Completable.error(new Throwable(Utilities.makeLog("cltRef null.")));
    }
    return Completable.create(
        emitter -> cltRef.document(data.getPrimaryKey()).get().addOnCompleteListener(task -> {
          DocumentSnapshot docSnap = task.getResult();
          if (docSnap == null) {
            emitter.onError(new Throwable(Utilities.makeLog("transmission error.")));
            return;
          } else if (!docSnap.exists()) {
            emitter.onError(new Throwable(Utilities.makeLog("data not existed.")));
            return;
          }
          T dataInDb = docSnap.toObject(dataClass);
          // data 동일 여부 체크.
          if (!dataInDb.equals(data)) {
            Utilities.log(LogType.d, "data not equal verification [SUCCESS]" +
                "\nprimary key of data : " + data.getPrimaryKey());
            emitter.onComplete();
          } else {
            emitter.onError(new Throwable(Utilities.makeLog("data not equal verification [FAIL]" +
                "\nprimary key of data : " + data.getPrimaryKey())));
          }
        }));
  }

  // DB 에 데이터 추가/수정하는 함수.
  public void setData(T data, Transaction transaction) {
    String pathKey;
    if (data.getPrimaryKey().compareTo(Data.AUTO_GENERATED_KEY) == 0) {
      Utilities.log(LogType.d, "add new document with auto generated id.");
      DocumentReference docRefToAdd = cltRef.document(); // 새 문서 생성. (id 자동 생성.)
      pathKey = docRefToAdd.getId();
    } else {
      pathKey = data.getPrimaryKey();
    }
    data.setPrimaryKey(pathKey);
    transaction.set(cltRef.document(pathKey), data);
  }

  // DB 에서 데이터 삭제하는 함수.
  public void deleteData(T data, Transaction transaction) {
    transaction.delete(cltRef.document(data.getPrimaryKey()));
  }

  public void deleteData(String primaryKey, Transaction transaction) {
    transaction.delete(cltRef.document(primaryKey));
  }

  public void setServerTimestamp(T data, String fieldName, Transaction transaction)
      throws FirebaseFirestoreException {
    transaction
        .update(cltRef.document(data.getPrimaryKey()), fieldName, FieldValue.serverTimestamp());
  }

  public void turnOffListener() {
    if (this.lisReg != null) {
      this.lisReg.remove();
    }
  }

  public Single<T> getData(String primaryKey) {
    return Single.<T>defer(() -> Single.create(emitter -> cltRef.document(primaryKey).get()
        .addOnSuccessListener(docSnap -> {
          if (!docSnap.exists()) {
            emitter.onError(new Throwable(Utilities.makeLog("not existed.")));
          } else {
            emitter.onSuccess(Objects.requireNonNull(docSnap.toObject(dataClass)));
          }
        })
        .addOnFailureListener(err -> Utilities.log(LogType.w, "err : " + err.getMessage()))))
        .subscribeOn(Schedulers.io());
  }
}
