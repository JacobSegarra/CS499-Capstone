package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for Food entity.
 * Provides methods to interact with the foods table.
 */
@Dao
public interface FoodDao {

    // ===== INSERT =====

    @Insert
    long insert(Food food);

    @Insert
    List<Long> insertAll(List<Food> foods);

    // ===== UPDATE =====

    @Update
    void update(Food food);

    // ===== DELETE =====

    @Delete
    void delete(Food food);

    @Query("DELETE FROM foods WHERE id = :foodId")
    void deleteById(int foodId);

    // ===== QUERIES =====

    /**
     * Get all foods (database + user custom foods).
     */
    @Query("SELECT * FROM foods ORDER BY name ASC")
    LiveData<List<Food>> getAllFoods();

    /**
     * Get food by ID.
     */
    @Query("SELECT * FROM foods WHERE id = :foodId")
    LiveData<Food> getFoodById(int foodId);

    /**
     * Get food by ID (non-LiveData for immediate access).
     */
    @Query("SELECT * FROM foods WHERE id = :foodId")
    Food getFoodByIdSync(int foodId);

    /**
     * Search foods by name.
     */
    @Query("SELECT * FROM foods WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    LiveData<List<Food>> searchFoodsByName(String searchQuery);

    /**
     * Get foods by category.
     */
    @Query("SELECT * FROM foods WHERE category = :category ORDER BY name ASC")
    LiveData<List<Food>> getFoodsByCategory(String category);

    /**
     * Get only database foods (not user-created).
     */
    @Query("SELECT * FROM foods WHERE isCustom = 0 ORDER BY name ASC")
    LiveData<List<Food>> getDatabaseFoods();

    /**
     * Get user's custom foods.
     */
    @Query("SELECT * FROM foods WHERE userId = :userId AND isCustom = 1 ORDER BY name ASC")
    LiveData<List<Food>> getUserCustomFoods(int userId);

    /**
     * Get all categories (distinct).
     */
    @Query("SELECT DISTINCT category FROM foods WHERE category IS NOT NULL ORDER BY category ASC")
    LiveData<List<String>> getAllCategories();

    /**
     * Count total foods.
     */
    @Query("SELECT COUNT(*) FROM foods")
    LiveData<Integer> getFoodCount();

    /**
     * Check if food exists by name (for preventing duplicates).
     */
    @Query("SELECT COUNT(*) FROM foods WHERE name = :name AND userId = :userId")
    int checkFoodExists(String name, int userId);
}