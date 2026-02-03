package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for MealFood entity (join table).
 * Links meals to foods with serving information.
 */
@Dao
public interface MealFoodDao {

    // ===== INSERT =====

    @Insert
    long insert(MealFood mealFood);

    @Insert
    List<Long> insertAll(List<MealFood> mealFoods);

    // ===== UPDATE =====

    @Update
    void update(MealFood mealFood);

    // ===== DELETE =====

    @Delete
    void delete(MealFood mealFood);

    @Query("DELETE FROM meal_foods WHERE id = :mealFoodId")
    void deleteById(int mealFoodId);

    /**
     * Delete all foods from a specific meal.
     */
    @Query("DELETE FROM meal_foods WHERE mealId = :mealId")
    void deleteAllForMeal(int mealId);

    // ===== QUERIES =====

    /**
     * Get all foods for a specific meal.
     */
    @Query("SELECT * FROM meal_foods WHERE mealId = :mealId")
    LiveData<List<MealFood>> getFoodsForMeal(int mealId);

    /**
     * Get all foods for a specific meal (non-LiveData).
     */
    @Query("SELECT * FROM meal_foods WHERE mealId = :mealId")
    List<MealFood> getFoodsForMealSync(int mealId);

    /**
     * Get a specific meal_food entry.
     */
    @Query("SELECT * FROM meal_foods WHERE id = :mealFoodId")
    LiveData<MealFood> getMealFoodById(int mealFoodId);

    /**
     * Get all meals that contain a specific food.
     */
    @Query("SELECT * FROM meal_foods WHERE foodId = :foodId")
    LiveData<List<MealFood>> getMealsContainingFood(int foodId);

    /**
     * Count foods in a meal.
     */
    @Query("SELECT COUNT(*) FROM meal_foods WHERE mealId = :mealId")
    LiveData<Integer> getFoodCountForMeal(int mealId);

    /**
     * Get total grams consumed for a meal.
     */
    @Query("SELECT SUM(gramsConsumed) FROM meal_foods WHERE mealId = :mealId")
    LiveData<Double> getTotalGramsForMeal(int mealId);

    /**
     * Check if a food is already in a meal (prevent duplicates).
     */
    @Query("SELECT COUNT(*) FROM meal_foods WHERE mealId = :mealId AND foodId = :foodId")
    int checkFoodInMeal(int mealId, int foodId);

    /**
     * Get foods with their details (JOIN query).
     * Returns MealFood entries with actual Food info.
     */
    @Query("SELECT mf.*, f.name, f.calories, f.protein, f.carbs, f.fats " +
            "FROM meal_foods mf " +
            "INNER JOIN foods f ON mf.foodId = f.id " +
            "WHERE mf.mealId = :mealId")
    LiveData<List<MealFoodWithDetails>> getMealFoodsWithDetails(int mealId);

    /**
     * Simple POJO for join query results.
     * This will hold both MealFood data and Food data together.
     */
    class MealFoodWithDetails {
        public int id;
        public int mealId;
        public int foodId;
        public double servings;
        public double gramsConsumed;

        // Food details
        public String name;
        public double calories;
        public double protein;
        public double carbs;
        public double fats;

        /**
         * Calculate nutrition for this serving.
         */
        public double[] calculateNutrition() {
            double ratio = gramsConsumed / 100.0;  // Nutrition is per 100g
            return new double[]{
                    calories * ratio,
                    protein * ratio,
                    carbs * ratio,
                    fats * ratio
            };
        }
    }
}