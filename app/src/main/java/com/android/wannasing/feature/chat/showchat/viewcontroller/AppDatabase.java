package com.android.wannasing.feature.chat.showchat.viewcontroller;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.android.wannasing.feature.chat.showchat.model.Chat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// exportSchema = setting for data migration.
@Database(entities = {Chat.class}, version = 1, exportSchema = false)
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
}
