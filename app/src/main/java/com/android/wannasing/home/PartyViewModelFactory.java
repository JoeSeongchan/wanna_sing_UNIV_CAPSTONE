package com.android.wannasing.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;
import com.google.firebase.firestore.FirebaseFirestore;

public class PartyViewModelFactory implements Factory {

  private final FirebaseFirestore fireDb;
  private final int pageSize;

  public PartyViewModelFactory(FirebaseFirestore fireDb, int pageSize) {
    this.fireDb = fireDb;
    this.pageSize = pageSize;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
    if (aClass.isAssignableFrom(PartyViewModel.class)) {
      return (T) new PartyViewModel(fireDb, pageSize);
    } else {
      throw new IllegalArgumentException("illegal argument type");
    }
  }
}
