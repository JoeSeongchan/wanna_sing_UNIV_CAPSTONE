package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;

import android.app.Application;
import com.android.wannasing.common.viewcontroller.old.AppDatabase;
import com.android.wannasing.common.viewcontroller.old.DbTracker;
import com.android.wannasing.common.viewcontroller.old.ServerDb;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.utility.Utilities;
import com.android.wannasing.utility.Utilities.LogType;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class BriefChatGroupDbTracker implements DbTracker<ChatGroup> {

  private final ServerDb<ChatGroup> serverDb;
  private final BriefChatGroupDao dao;
  Disposable dispUpdate;

  public BriefChatGroupDbTracker(String fullPath, Application application) {
    this.serverDb = new ServerDb<>(fullPath, ChatGroup.class);
    this.dao = AppDatabase.getDatabase(application).briefChatGroupDao();
  }

  public void getUpdateInRealTime() {
    dispose();
    // 데이터 일관성 체크.
    dispUpdate = keepRoomDbConsistentWithFireDb()
        // 실시간으로 데이터 추적.
        .andThen(serverDb.getUpdateInRealTime())
        .observeOn(Schedulers.io())
        // Room DB 조작.
        .flatMapCompletable(dbChange -> {
          Utilities.log(LogType.d, "change!");
          switch (dbChange.getType()) {
            case ADD:
              return dao.insert(dbChange.getData());
            case MODIFY:
              return dao.update(dbChange.getData());
            case DELETE:
              return dao.delete(dbChange.getData());
            default:
              return Completable
                  .error(new Throwable(Utilities.makeLog("not compatible type.")));
          }
        })
        .subscribe(() -> Utilities.log(LogType.d, "done."),
            err -> Utilities.log(LogType.w, err.getMessage()));
  }

  private Completable keepRoomDbConsistentWithFireDb() {
//    update state 초기화. true ->false.
    return this.dao.resetUpdateState()
        .subscribeOn(Schedulers.io())
        // 서버에서 모든 데이터 가져오기.
        .andThen(serverDb.getAllItem())
        .observeOn(Schedulers.computation())
        // primary key 만 뽑아내기.
        .map(items -> {
          List<String> primaryKeys = new ArrayList<>();
          for (ChatGroup item : items) {
            primaryKeys.add(item.getPrimaryKey());
          }
          return primaryKeys;
        }).observeOn(Schedulers.io())
        // 서버에 있는 데이터만 체크.
        .flatMapCompletable(
            primaryKeys ->
                Completable.defer(() -> this.dao.markUpdatedItems(primaryKeys)))
        // 서버에 없는 데이터 제거.
        .andThen(this.dao.deleteNotUpdated());
  }

  @Override
  public void dispose() {
    if (dispUpdate != null && !dispUpdate.isDisposed()) {
      dispUpdate.dispose();
    }
  }
}
