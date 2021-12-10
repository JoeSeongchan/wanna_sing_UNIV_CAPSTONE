package com.android.wannasing.feature.chat.showchat.viewcontroller;


import com.android.wannasing.feature.chat.showchat.model.Data;

public interface DbTracker<T extends Data> {

  void getUpdateInRealTime();

  void dispose();
}
