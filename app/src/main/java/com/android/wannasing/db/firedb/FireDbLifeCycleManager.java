package com.android.wannasing.db.firedb;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.List;

public class FireDbLifeCycleManager implements DefaultLifecycleObserver {

  List<FireDb<?>> fireDbList;

  public FireDbLifeCycleManager() {
    fireDbList = new ArrayList<>();
  }

  public void add(FireDb<?> serverDb) {
    fireDbList.add(serverDb);
  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {
    for (FireDb<?> serverDb : fireDbList) {
      serverDb.turnOffListener();
    }
  }
}
