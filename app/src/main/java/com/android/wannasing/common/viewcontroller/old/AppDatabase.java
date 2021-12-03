package com.android.wannasing.common.viewcontroller.old;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.android.wannasing.common.model.old.Party;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import com.android.wannasing.feature.chat.showchat.viewcontroller.ChatDao;
import com.android.wannasing.feature.chat.showchatgroup.model.ChatGroup;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.BriefChatGroupDao;
import com.android.wannasing.feature.chat.showchatgroup.viewcontroller.PartyDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// exportSchema = setting for data migration.
@Deprecated
@Database(entities = {Chat.class, ChatGroup.class, Party.class}, version = 1, exportSchema = false)
@TypeConverters({TypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

  private static final int NUMBER_OF_THREADS = 4;
  public static final ExecutorService databaseWriteExecutor =
      Executors.newFixedThreadPool(NUMBER_OF_THREADS);

  private static final Callback dbClb;

  // singleton pattern,
  private static volatile AppDatabase INSTANCE;

  static {
    dbClb = new Callback() {
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        databaseWriteExecutor.execute(() -> {
        });
      }
    };
  }

  public static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              AppDatabase.class, "app_database")
              .addCallback(dbClb)
              .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract ChatDao chatDao();

  public abstract BriefChatGroupDao briefChatGroupDao();

  public abstract PartyDao partyDao();

}
