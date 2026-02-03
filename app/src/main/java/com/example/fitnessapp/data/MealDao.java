package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for Meal entity.
 * Provides methods to interact with the meals table.
 */
@Dao
public interface MealDao {

    // ===== INSERT =====

    @Insert
    long insert(Meal meal);

    @Insert
    List<Long> insertAll(List<Meal> meals);

    // ===== UPDATE =====

    @Update
    void update(Meal meal);

    // ===== DELETE =====

    @Delete
    void delete(Meal meal);

    @Query("DELETE FROM meals WHERE id = :mealId")
    void deleteById(int mealId);

    /**
     * Delete all meals for a specific date.
     */
    @Query("DELETE FROM meals WHERE userId = :userId AND date = :date")
    void deleteMealsByDate(int userId, String date);

    // ===== QUERIES =====

    /**
     * Get all meals for a user.
     */
    @Query("SELECT * FROM meals WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<Meal>> getAllMealsForUser(int userId);

    /**
     * Get meals for a specific date.
     */
    @Query("SELECT * FROM meals WHERE userId = :userId AND date = :date ORDER BY timestamp ASC")
    LiveData<List<Meal>> getMealsByDate(int userId, String date);

    /**
     * Get meals for a specific date (non-LiveData for immediate access).
     */
    @Query("SELECT * FROM meals WHERE userId = :userId AND date = :date ORDER BY timestamp ASC")
    List<Meal> getMealsByDateSync(int userId, String date);

    /**
     * Get meal by ID.
     */
    @Query("SELECT * FROM meals WHERE id = :mealId")
    LiveData<Meal> getMealById(int mealId);

    /**
     * Get meal by ID (non-LiveData).
     */
    @Query("SELECT * FROM meals WHERE id = :mealId")
    Meal getMealByIdSync(int mealId);

    /**
     * Get meals by type for a specific date.
     */
    @Query("SELECT * FROM meals WHERE userId = :userId AND date = :date AND mealType = :mealType")
    LiveData<List<Meal>> getMealsByTypeAndDate(int userId, String date, String mealType);

    /**
     * Get meals within a date range.
     */
    @Query("SELECT * FROM meals WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC, timestamp ASC")
    LiveData<List<Meal>> getMealsInDateRange(int userId, String startDate, String endDate);

    /**
     * Get recent meals (last N meals).
     */
    @Query("SELECT * FROM meals WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<Meal>> getRecentMeals(int userId, int limit);

    /**
     * Count meals for today.
     */
    @Query("SELECT COUNT(*) FROM meals WHERE userId = :userId AND date = :date")
    LiveData<Integer> getMealCountForDate(int userId, String date);

    /**
     * Get total calories for a specific date.
     */
    @Query("SELECT SUM(totalCalories) FROM meals WHERE userId = :userId AND date = :date")
    LiveData<Double> getTotalCaloriesForDate(int userId, String date);

    /**
     * Check if a meal type exists for a date (to prevent duplicates).
     */
    @Query("SELECT COUNT(*) FROM meals WHERE userId = :userId AND date = :date AND mealType = :mealType")
    int checkMealExists(int userId, String date, String mealType);

    /**
     * Get all unique dates where user has logged meals.
     */
    @Query("SELECT DISTINCT date FROM meals WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<String>> getDatesWithMeals(int userId);
}