package com.android.wannasing.common.viewcontroller;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.List;

public class FireDbLifeCycleObserver implements DefaultLifecycleObserver {

  List<FireDb<?>> serverDb;

  public FireDbLifeCycleObserver() {
    serverDb = new ArrayList<>();
  }

  public void add(FireDb<?> serverDb) {
    this.serverDb.add(serverDb);
  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {
    for (FireDb<?> serverDb : serverDb) {
      serverDb.turnOffListener();
    }
  }
}
