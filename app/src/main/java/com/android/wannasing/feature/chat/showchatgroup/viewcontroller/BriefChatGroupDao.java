package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;

@Dao
public interface BriefChatGroupDao {

  @Query("SELECT * FROM chat_group_table")
  LiveData<List<ChatGroup>> getAll();

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Completable insert(ChatGroup item);

  @Query("DELETE FROM chat_group_table")
  Completable deleteAll();

  @Delete
  Completable delete(ChatGroup item);

  @Update
  Completable update(ChatGroup item);

  @Query("UPDATE chat_group_table SET isUpdated = 'true' WHERE id IN (:ids)")
  Completable markUpdatedItems(List<String> ids);

  @Query("DELETE FROM chat_group_table WHERE isUpdated = 'false'")
  Completable deleteNotUpdated();

  @Query("UPDATE chat_group_table SET isUpdated = 'false'")
  Completable resetUpdateState();

  @Query("UPDATE chat_group_table SET lastMsg = :lastMsg WHERE id = :groupId")
  Completable updateLastMsg(String groupId, String lastMsg);
}
