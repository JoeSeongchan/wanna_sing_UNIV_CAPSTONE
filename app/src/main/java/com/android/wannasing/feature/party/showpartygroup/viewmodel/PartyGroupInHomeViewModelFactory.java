package com.android.wannasing.feature.party.showpartygroup.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;
import com.google.firebase.firestore.FirebaseFirestore;

public class PartyGroupInHomeViewModelFactory implements Factory {

  private final FirebaseFirestore fireDb;

  public PartyGroupInHomeViewModelFactory(FirebaseFirestore fireDb) {
    this.fireDb = fireDb;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
    if (aClass.isAssignableFrom(
        PartyGroupInHomeViewModel.class)) {
      return (T) new PartyGroupInHomeViewModel(fireDb);
    } else {
      throw new IllegalArgumentException("illegal argument type");
    }
  }
}
