package com.android.wannasing.home.fragment.partygroup.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;
import com.google.firebase.firestore.FirebaseFirestore;

public class PartyGroupInHomeViewModelFactory implements Factory {

  private final FirebaseFirestore fireDb;
  private final int pageSize;

  public PartyGroupInHomeViewModelFactory(FirebaseFirestore fireDb, int pageSize) {
    this.fireDb = fireDb;
    this.pageSize = pageSize;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
    if (aClass.isAssignableFrom(PartyGroupInHomeViewModel.class)) {
      return (T) new PartyGroupInHomeViewModel(fireDb, pageSize);
    } else {
      throw new IllegalArgumentException("illegal argument type");
    }
  }
}
