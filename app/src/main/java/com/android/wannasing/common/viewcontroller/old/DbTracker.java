package com.android.wannasing.common.viewcontroller.old;


import com.android.wannasing.common.model.old.Data;

@Deprecated
public interface DbTracker<T extends Data> {

  void getUpdateInRealTime();

  void dispose();
}
