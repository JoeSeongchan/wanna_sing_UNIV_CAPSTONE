package com.android.wannasing.feature.chat.showchatgroup.viewcontroller;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.android.wannasing.common.model.old.Party;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;

@Dao
public interface PartyDao {

  @Query("SELECT * FROM party_table")
  LiveData<List<Party>> getAll();

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Completable insert(Party data);

  @Query("DELETE FROM party_table")
  Completable deleteAll();

  @Delete
  Completable delete(Party item);

  @Update
  Completable update(Party item);

  @Query("UPDATE party_table SET isUpdated = 'true' WHERE id IN (:ids)")
  Completable markUpdatedItems(List<String> ids);

  @Query("DELETE FROM party_table WHERE isUpdated = 'false'")
  Completable deleteNotUpdated();

  @Query("UPDATE party_table SET isUpdated = 'false'")
  Completable resetUpdateState();
}
