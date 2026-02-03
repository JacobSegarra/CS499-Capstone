package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for DailyNutritionSummary entity.
 * Manages daily nutrition totals (one record per user per day).
 */
@Dao
public interface DailyNutritionSummaryDao {

    // ===== INSERT =====

    /**
     * Insert or replace if already exists (due to unique constraint).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DailyNutritionSummary summary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<DailyNutritionSummary> summaries);

    // ===== UPDATE =====

    @Update
    void update(DailyNutritionSummary summary);

    // ===== DELETE =====

    @Delete
    void delete(DailyNutritionSummary summary);

    @Query("DELETE FROM daily_nutrition_summary WHERE id = :summaryId")
    void deleteById(int summaryId);

    /**
     * Delete summary for a specific date.
     */
    @Query("DELETE FROM daily_nutrition_summary WHERE userId = :userId AND date = :date")
    void deleteSummaryForDate(int userId, String date);

    // ===== QUERIES =====

    /**
     * Get summary for a specific date.
     * This is the most common query - "How did I do today?"
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId AND date = :date")
    LiveData<DailyNutritionSummary> getSummaryForDate(int userId, String date);

    /**
     * Get summary for a specific date (non-LiveData for immediate access).
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId AND date = :date")
    DailyNutritionSummary getSummaryForDateSync(int userId, String date);

    /**
     * Get all summaries for a user (entire history).
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<DailyNutritionSummary>> getAllSummaries(int userId);

    /**
     * Get summaries within a date range (for weekly/monthly views).
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    LiveData<List<DailyNutritionSummary>> getSummariesInRange(int userId, String startDate, String endDate);

    /**
     * Get summaries within a date range (non-LiveData).
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    List<DailyNutritionSummary> getSummariesInRangeSync(int userId, String startDate, String endDate);

    /**
     * Get recent summaries (last N days).
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId ORDER BY date DESC LIMIT :limit")
    LiveData<List<DailyNutritionSummary>> getRecentSummaries(int userId, int limit);

    /**
     * Get average calories over a date range.
     */
    @Query("SELECT AVG(totalCalories) FROM daily_nutrition_summary WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getAverageCaloriesInRange(int userId, String startDate, String endDate);

    /**
     * Get average protein over a date range.
     */
    @Query("SELECT AVG(totalProtein) FROM daily_nutrition_summary WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getAverageProteinInRange(int userId, String startDate, String endDate);

    /**
     * Count days with logged nutrition.
     */
    @Query("SELECT COUNT(*) FROM daily_nutrition_summary WHERE userId = :userId")
    LiveData<Integer> getTotalDaysLogged(int userId);

    /**
     * Get days where calorie target was met (within threshold).
     */
    @Query("SELECT COUNT(*) FROM daily_nutrition_summary WHERE userId = :userId AND totalCalories BETWEEN :targetCalories - :threshold AND :targetCalories + :threshold")
    LiveData<Integer> getDaysCalorieTargetMet(int userId, double targetCalories, double threshold);

    /**
     * Get total meals logged across all days.
     */
    @Query("SELECT SUM(mealsLogged) FROM daily_nutrition_summary WHERE userId = :userId")
    LiveData<Integer> getTotalMealsLogged(int userId);

    /**
     * Get all dates with logged nutrition (for calendar view).
     */
    @Query("SELECT DISTINCT date FROM daily_nutrition_summary WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<String>> getDatesWithNutrition(int userId);

    /**
     * Get highest calorie day.
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId ORDER BY totalCalories DESC LIMIT 1")
    LiveData<DailyNutritionSummary> getHighestCalorieDay(int userId);

    /**
     * Get best protein day.
     */
    @Query("SELECT * FROM daily_nutrition_summary WHERE userId = :userId ORDER BY totalProtein DESC LIMIT 1")
    LiveData<DailyNutritionSummary> getBestProteinDay(int userId);
}