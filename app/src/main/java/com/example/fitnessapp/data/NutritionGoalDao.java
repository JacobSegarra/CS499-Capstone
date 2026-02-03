package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * DAO for NutritionGoal entity.
 * Manages user's personalized nutrition targets.
 * Each user has only one goal (updated when settings change).
 */
@Dao
public interface NutritionGoalDao {

    // ===== INSERT =====

    /**
     * Insert or replace if already exists (one goal per user).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NutritionGoal goal);

    // ===== UPDATE =====

    @Update
    void update(NutritionGoal goal);

    // ===== DELETE =====

    @Delete
    void delete(NutritionGoal goal);

    @Query("DELETE FROM nutrition_goals WHERE id = :goalId")
    void deleteById(int goalId);

    /**
     * Delete goal for a specific user.
     */
    @Query("DELETE FROM nutrition_goals WHERE userId = :userId")
    void deleteGoalForUser(int userId);

    // ===== QUERIES =====

    /**
     * Get nutrition goal for a user.
     * This is the most common query - returns the user's active goal.
     */
    @Query("SELECT * FROM nutrition_goals WHERE userId = :userId")
    LiveData<NutritionGoal> getGoalForUser(int userId);

    /**
     * Get nutrition goal for a user (non-LiveData for immediate access).
     */
    @Query("SELECT * FROM nutrition_goals WHERE userId = :userId")
    NutritionGoal getGoalForUserSync(int userId);

    /**
     * Get goal by ID.
     */
    @Query("SELECT * FROM nutrition_goals WHERE id = :goalId")
    LiveData<NutritionGoal> getGoalById(int goalId);

    /**
     * Check if user has a goal set.
     */
    @Query("SELECT COUNT(*) FROM nutrition_goals WHERE userId = :userId")
    int hasGoal(int userId);

    /**
     * Get calorie target for user (quick access).
     */
    @Query("SELECT calorieTarget FROM nutrition_goals WHERE userId = :userId")
    LiveData<Double> getCalorieTarget(int userId);

    /**
     * Get protein target for user (quick access).
     */
    @Query("SELECT proteinTarget FROM nutrition_goals WHERE userId = :userId")
    LiveData<Double> getProteinTarget(int userId);

    /**
     * Get all targets as array (quick access).
     */
    @Query("SELECT calorieTarget, proteinTarget, carbsTarget, fatsTarget FROM nutrition_goals WHERE userId = :userId")
    double[] getAllTargets(int userId);

    /**
     * Update only the calculated values (when recalculating with same profile).
     */
    @Query("UPDATE nutrition_goals SET bmr = :bmr, tdee = :tdee, calorieTarget = :calorieTarget, " +
            "proteinTarget = :proteinTarget, carbsTarget = :carbsTarget, fatsTarget = :fatsTarget, " +
            "waterTarget = :waterTarget, calculatedAt = :calculatedAt WHERE userId = :userId")
    void updateCalculatedValues(int userId, double bmr, double tdee, double calorieTarget,
                                double proteinTarget, double carbsTarget, double fatsTarget,
                                double waterTarget, long calculatedAt);

    /**
     * Update user profile info (when user changes height, age, etc.).
     */
    @Query("UPDATE nutrition_goals SET height = :height, age = :age, gender = :gender, " +
            "activityLevel = :activityLevel, goal = :goal WHERE userId = :userId")
    void updateProfile(int userId, double height, int age, String gender,
                       String activityLevel, String goal);

    /**
     * Check if goal needs recalculation (older than 30 days).
     */
    @Query("SELECT ((:currentTime - calculatedAt) > 2592000000) FROM nutrition_goals WHERE userId = :userId")
    boolean needsRecalculation(int userId, long currentTime);

    /**
     * Get goals by fitness goal type (for statistics/comparisons).
     */
    @Query("SELECT * FROM nutrition_goals WHERE goal = :goalType")
    LiveData<java.util.List<NutritionGoal>> getGoalsByType(String goalType);

    /**
     * Count users by goal type (for app analytics).
     */
    @Query("SELECT COUNT(*) FROM nutrition_goals WHERE goal = :goalType")
    LiveData<Integer> countUsersByGoalType(String goalType);
}