package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for the User entity
 * These methods are called by repositories
 * Authentication is now handled in the repository layer using BCrypt verification,
 * not in the DAO with SQL queries
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

    // --- Query Methods ---

    /**
     * Gets a user by username only (for authentication and registration checks).
     * Password verification happens in the repository layer using BCrypt.
     *
     * @param username The username to search for
     * @return The User object if found, null otherwise
     */
    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    /**
     * Checks if a username already exists (for registration validation).
     *
     * @param username The username to check
     * @return List of users with this username (should be 0 or 1 due to unique constraint)
     */
    @Query("SELECT * FROM user_table WHERE username = :username")
    List<User> checkUsernameExists(String username);

    /**
     * Gets a user by their ID as LiveData (used by activities with observers).
     *
     * @param userId The user's ID
     * @return LiveData containing the User object
     */
    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    LiveData<User> getUserById(int userId);

    /**
     * Gets a user by their ID synchronously (for repository operations).
     *
     * @param userId The user's ID
     * @return The User object if found, null otherwise
     */
    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    User getUserByIdSync(int userId);
}