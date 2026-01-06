package com.example.optiononeweighttrackingappjacobsegarra.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for the User entity.
 * These methods are called directly by the EntryRepository.
 */
@Dao
public interface UserDao {

    // --- Basic CRUD operations ---

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    // --- Authentication and Registration Queries ---

    /**
     * Used for user login. Finds a user matching both the username and password.
     * Since this is used for synchronous login, it returns the User object directly (or null).
     * @param username The username provided.
     * @param password The password provided.
     * @return The matching User object, or null if no match is found.
     */
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    User getUserByUsernameAndPassword(String username, String password);

    /**
     * Used during user registration to check for username uniqueness.
     * It returns a list.
     * @param username The username to check.
     * @return A list of users matching the username.
     */
    @Query("SELECT * FROM user_table WHERE username = :username")
    List<User> getUserByUsername(String username);

    /**
     * Gets a user by their ID as LiveData (used by MainActivity and SettingsActivity).
     * @param userId The user's ID.
     * @return LiveData containing the User object.
     */
    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    LiveData<User> getUserById(int userId);
}