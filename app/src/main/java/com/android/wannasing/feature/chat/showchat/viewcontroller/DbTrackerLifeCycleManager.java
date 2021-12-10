package com.android.wannasing.feature.chat.showchat.viewcontroller;

import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.util.ArrayList;
import java.util.List;

public class DbTrackerLifeCycleManager implements LifecycleObserver {

  List<DbTracker<?>> dbTrackerList;

  public DbTrackerLifeCycleManager() {
    dbTrackerList = new ArrayList<>();
  }

  public void add(DbTracker<?> dbTracker) {
    dbTrackerList.add(dbTracker);
  }

  @OnLifecycleEvent(Event.ON_DESTROY)
  void dispose() {
    dbTrackerList.forEach(DbTracker::dispose);
  }
}
