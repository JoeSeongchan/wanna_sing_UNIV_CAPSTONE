package com.android.wannasing.common.viewcontroller.old;


import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ServerDbLifeCycleManager implements LifecycleObserver {

  List<ServerDb<?>> serverDbList;

  public ServerDbLifeCycleManager() {
    serverDbList = new ArrayList<>();
  }

  public void add(ServerDb<?> serverDb) {
    serverDbList.add(serverDb);
  }

  @OnLifecycleEvent(Event.ON_DESTROY)
  void turnOffListener() {
    for (ServerDb<?> serverDb : serverDbList) {
      serverDb.turnOffListener();
    }
  }
}
