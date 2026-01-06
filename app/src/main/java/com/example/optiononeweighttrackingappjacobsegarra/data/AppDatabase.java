package com.example.optiononeweighttrackingappjacobsegarra.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room Database implementation for the application.
 * It holds the database version, entities, and provides access to the DAOs.
 * Also manages the background thread pool for asynchronous database writes.
 */
@Database(entities = {User.class, WeightEntry.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Define the abstract DAO accessors
    public abstract UserDao userDao();
    public abstract WeightEntryDao weightEntryDao();

    // Singleton instance and name
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "weight_tracker_db";

    // Define the ExecutorService required by the EntryRepository
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets the singleton instance of the database.
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME)
                            // Allows Room to rebuild the database if schema changes
                            .fallbackToDestructiveMigration()
                            // Adding a callback ensures Room re-validates the schema
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback for the database creation/opening process.
     * This is an empty callback
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Leaving it empty for now.
        }
    };
}
