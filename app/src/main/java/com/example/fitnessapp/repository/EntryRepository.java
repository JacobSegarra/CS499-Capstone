package com.example.fitnessapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.data.AppDatabase;
import com.example.fitnessapp.data.User;
import com.example.fitnessapp.data.UserDao;
import com.example.fitnessapp.data.WeightEntry;
import com.example.fitnessapp.data.WeightEntryDao;
import com.example.fitnessapp.util.Result;
import com.example.fitnessapp.util.SecurityUtils;
import com.example.fitnessapp.util.ValidationUtils;

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
     * Authenticates a user by matching username and password
     * MUST be called on a background thread
     * @return The authenticated User object, or null
     */
    /**
     * Authenticates a user by verifying username and password with BCrypt
     * MUST be called on a background thread
     *
     * @param username The username to authenticate
     * @param plainPassword The plain-text password to verify
     * @return Result containing the authenticated User or error message
     */
    public Result<User> authenticateUser(String username, String plainPassword) {
        try {
            // Input validation
            if (username == null || username.trim().isEmpty()) {
                return Result.failure("Username cannot be empty");
            }
            if (plainPassword == null || plainPassword.trim().isEmpty()) {
                return Result.failure("Password cannot be empty");
            }

            // Get user by username
            User user = userDao.getUserByUsername(username);

            if (user == null) {
                return Result.failure("Invalid username or password");
            }

            // Verify password using BCrypt
            boolean passwordMatches = SecurityUtils.verifyPassword(plainPassword, user.getPasswordHash());

            if (passwordMatches) {
                return Result.success(user);
            } else {
                return Result.failure("Invalid username or password");
            }

        } catch (Exception e) {
            return Result.failure("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Inserts a new user into the database and returns the new ID
     * MUST be called synchronously on a background thread
     * @param user The user object to insert
     * @return The primary key ID of the inserted row
     */
    /**
     * Registers a new user with hashed password and validation
     * MUST be called on a background thread
     *
     * @param username The desired username
     * @param plainPassword The plain-text password (will be hashed)
     * @param goalWeight The user's goal weight
     * @param phoneNumber The user's phone number
     * @return Result containing the new User ID or error message
     */

    public Result<Long> registerUser(String username, String plainPassword, double goalWeight, String phoneNumber) {
        try {
            // Validate username
            ValidationUtils.ValidationResult usernameValidation = ValidationUtils.validateUsername(username);
            if (usernameValidation.isInvalid()) {
                return Result.failure(usernameValidation.getErrorMessage());
            }

            // Validate password
            ValidationUtils.ValidationResult passwordValidation = ValidationUtils.validatePassword(plainPassword);
            if (passwordValidation.isInvalid()) {
                return Result.failure(passwordValidation.getErrorMessage());
            }

            // Validate phone number
            ValidationUtils.ValidationResult phoneValidation = ValidationUtils.validatePhone(phoneNumber);
            if (phoneValidation.isInvalid()) {
                return Result.failure(phoneValidation.getErrorMessage());
            }

            // Validate goal weight
            ValidationUtils.ValidationResult goalWeightValidation = ValidationUtils.validateGoalWeight(goalWeight);
            if (goalWeightValidation.isInvalid()) {
                return Result.failure(goalWeightValidation.getErrorMessage());
            }

            // Check if username already exists
            List<User> existingUsers = userDao.checkUsernameExists(username);
            if (existingUsers != null && !existingUsers.isEmpty()) {
                return Result.failure("Username already exists");
            }

            // Hash the password
            String passwordHash = SecurityUtils.hashPassword(plainPassword);

            // Create new user
            User newUser = new User(username, passwordHash, goalWeight, phoneNumber);

            // Insert into database
            Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.insert(newUser));
            long userId = future.get();

            if (userId > 0) {
                return Result.success(userId);
            } else {
                return Result.failure("Failed to create user");
            }

        } catch (Exception e) {
            return Result.failure("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Checks if a username is available
     * MUST be called on a background thread
     *
     * @param username The username to check
     * @return true if available, false if taken
     */
    public boolean isUsernameAvailable(String username) {
        try {
            List<User> existingUsers = userDao.checkUsernameExists(username);
            return existingUsers == null || existingUsers.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    public long insertUser(User user) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.insert(user));
        try {
            return future.get();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Updates an existing User's settings (goal weight/phone number)
     * @param user The user object to update (must have the correct ID set)
     */
    public void updateUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.update(user));
    }

    /**
     * Gets a user by ID synchronously (for background operations)
     * MUST be called on a background thread
     *
     * @param userId The user ID
     * @return The User object or null
     */
    public User getUserByIdSync(int userId) {
        try {
            Future<User> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.getUserByIdSync(userId));
            return future.get();
        } catch (Exception e) {
            return null;
        }
    }
}