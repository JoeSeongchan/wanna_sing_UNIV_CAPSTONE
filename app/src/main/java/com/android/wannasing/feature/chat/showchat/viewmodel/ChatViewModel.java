package com.android.wannasing.feature.chat.showchat.viewmodel;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import com.android.wannasing.feature.chat.showchat.viewcontroller.ChatRepo;
import java.util.List;


public class ChatViewModel extends AndroidViewModel {

  private final LiveData<List<Chat>> allData;
  private ChatRepo repo;

  public ChatViewModel(@NonNull Application application) {
    super(application);
    repo = new ChatRepo(application);
    allData = repo.getAll();
  }

  // method to return a cached list of words.
  public LiveData<List<Chat>> getAll() {
    return allData;
  }

  public void insert(Chat item) {
    repo.insert(item);
  }

  public void delete(Chat item) {
    repo.delete(item);
  }

  public void deleteAll() {
    repo.deleteAll();
  }
}
