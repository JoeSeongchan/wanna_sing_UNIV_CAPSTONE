package com.android.wannasing.party.showinginfo.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberViewModelFactory implements ViewModelProvider.Factory {

  private final FirebaseFirestore fireDb;
  private final String hostId;
  private final String partyName;

  public MemberViewModelFactory(FirebaseFirestore fireDb, String hostId, String partyName) {
    this.fireDb = fireDb;
    this.hostId = hostId;
    this.partyName = partyName;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
    if (aClass.equals(MemberViewModel.class)) {
      return (T) new MemberViewModel(fireDb, hostId, partyName);
    } else {
      throw new IllegalArgumentException("illegal argument type");
    }
  }
}
