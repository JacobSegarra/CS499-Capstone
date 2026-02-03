package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity storing auto-calculated daily nutrition totals.
 * One record per user per day, updated when meals are logged.
 */
@Entity(
        tableName = "daily_nutrition_summary",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "userId"),
                @Index(value = "date"),
                @Index(value = {"userId", "date"}, unique = true)  // One record per user per day
        }
)
public class DailyNutritionSummary {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String date;             // 'YYYY-MM-DD'

    // Daily totals (sum of all meals for this day)
    private double totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFats;

    private int mealsLogged;         // How many meals logged today
    private long lastUpdated;        // Timestamp of last update

    // Constructor
    public DailyNutritionSummary(int userId, String date) {
        this.userId = userId;
        this.date = date;
        this.totalCalories = 0.0;
        this.totalProtein = 0.0;
        this.totalCarbs = 0.0;
        this.totalFats = 0.0;
        this.mealsLogged = 0;
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(double totalProtein) {
        this.totalProtein = totalProtein;
    }

    public double getTotalCarbs() {
        return totalCarbs;
    }

    public void setTotalCarbs(double totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public double getTotalFats() {
        return totalFats;
    }

    public void setTotalFats(double totalFats) {
        this.totalFats = totalFats;
    }

    public int getMealsLogged() {
        return mealsLogged;
    }

    public void setMealsLogged(int mealsLogged) {
        this.mealsLogged = mealsLogged;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Update daily totals with a meal's nutrition.
     */
    public void addMealNutrition(double calories, double protein, double carbs, double fats) {
        this.totalCalories += calories;
        this.totalProtein += protein;
        this.totalCarbs += carbs;
        this.totalFats += fats;
        this.mealsLogged++;
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * Calculate remaining calories vs. target.
     */
    public double getRemainingCalories(double targetCalories) {
        return targetCalories - this.totalCalories;
    }

    /**
     * Get formatted summary string.
     */
    public String getSummary() {
        return String.format("%.0f cal | P: %.0fg | C: %.0fg | F: %.0fg | %d meals",
                totalCalories, totalProtein, totalCarbs, totalFats, mealsLogged);
    }

    @Override
    public String toString() {
        return "DailyNutritionSummary{" + date + ", " +
                String.format("%.0f cal", totalCalories) +
                ", " + mealsLogged + " meals}";
    }
}