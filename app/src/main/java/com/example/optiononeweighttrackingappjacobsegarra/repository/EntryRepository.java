package com.example.optiononeweighttrackingappjacobsegarra.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.optiononeweighttrackingappjacobsegarra.data.AppDatabase;
import com.example.optiononeweighttrackingappjacobsegarra.data.User;
import com.example.optiononeweighttrackingappjacobsegarra.data.UserDao;
import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntry;
import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntryDao;

import java.util.List;
import java.util.concurrent.Future;

public class EntryRepository {

    private final WeightEntryDao weightEntryDao;
    private final UserDao userDao;

    // --- Constructor ---
    public EntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        weightEntryDao = db.weightEntryDao();
        userDao = db.userDao();
    }

    // --- LiveData Fetches (Used by ViewModel) ---

    /**
     * Retrieves all weight entries for a specific user
     */
    public LiveData<List<WeightEntry>> getAllEntriesForUser(int userId) {
        return weightEntryDao.getAllEntriesForUser(userId);
    }

    /**
     * Retrieves the current User object
     */
    public LiveData<User> getCurrentUser(int userId) {
        return userDao.getUserById(userId);
    }

    // --- WeightEntry CRUD Operations ---

    public void insertWeightEntry(WeightEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> weightEntryDao.insert(entry));
    }

    public void updateWeightEntry(WeightEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> weightEntryDao.update(entry));
    }

    public void deleteWeightEntry(WeightEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> weightEntryDao.delete(entry));
    }

    // --- User Operations ---

    /**
     * Authenticates a user by matching username and password.
     * MUST be called on a background thread.
     * @return The authenticated User object, or null.
     */
    public User authenticateUser(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    /**
     * Checks if a user with the given username already exists.
     * MUST be called synchronously on a background thread.
     * @param username The username to check.
     * @return A list of matching users.
     */
    public List<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    /**
     * Inserts a new user into the database and returns the new ID.
     * MUST be called synchronously on a background thread.
     * @param user The user object to insert.
     * @return The primary key ID of the inserted row.
     */
    public long insertUser(User user) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.insert(user));
        try {
            return future.get();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Updates an existing User's settings (goal weight/phone number).
     * @param user The user object to update (must have the correct ID set).
     */
    public void updateUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.update(user));
    }
}