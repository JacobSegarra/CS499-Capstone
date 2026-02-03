package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a meal (breakfast, lunch, dinner, or snack).
 * Total nutrition values are calculated from associated meal_foods.
 */
@Entity(
        tableName = "meals",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "userId"),
                @Index(value = "date"),
                @Index(value = {"userId", "date"})
        }
)
public class Meal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String mealType;         // 'breakfast', 'lunch', 'dinner', 'snack'
    private String date;             // 'YYYY-MM-DD' format
    private long timestamp;
    private String notes;

    // Total nutrition (calculated from meal_foods)
    private double totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFats;

    // Constructor
    public Meal(int userId, String mealType, String date) {
        this.userId = userId;
        this.mealType = mealType;
        this.date = date;
        this.timestamp = System.currentTimeMillis();
        this.totalCalories = 0.0;
        this.totalProtein = 0.0;
        this.totalCarbs = 0.0;
        this.totalFats = 0.0;
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

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    /**
     * Update meal totals by adding food nutrition.
     */
    public void addFoodNutrition(double calories, double protein, double carbs, double fats) {
        this.totalCalories += calories;
        this.totalProtein += protein;
        this.totalCarbs += carbs;
        this.totalFats += fats;
    }

    /**
     * Get formatted meal type for display.
     */
    public String getMealTypeDisplay() {
        if (mealType == null) return "Unknown";
        return mealType.substring(0, 1).toUpperCase() + mealType.substring(1);
    }

    @Override
    public String toString() {
        return "Meal{" + mealType + " on " + date + ", " +
                String.format("%.0f cal", totalCalories) + "}";
    }
}