package com.android.wannasing.feature.chat.showchatgroup.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;
import com.android.wannasing.common.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowChatGroupViewModelFactory implements Factory {

  private final FirebaseFirestore fireDb;
  private final User user;

  public ShowChatGroupViewModelFactory(FirebaseFirestore fireDb, User user) {
    this.fireDb = fireDb;
    this.user = user;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
    if (aClass.isAssignableFrom(
        ShowChatGroupViewModel.class)) {
      return (T) new ShowChatGroupViewModel(fireDb, user);
    } else {
      throw new IllegalArgumentException("illegal argument type");
    }
  }
}
