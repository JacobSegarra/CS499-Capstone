package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Join table linking meals to foods with serving information.
 * Represents: "This meal contains X grams of this food."
 */
@Entity(
        tableName = "meal_foods",
        foreignKeys = {
                @ForeignKey(
                        entity = Meal.class,
                        parentColumns = "id",
                        childColumns = "mealId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Food.class,
                        parentColumns = "id",
                        childColumns = "foodId",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index(value = "mealId"),
                @Index(value = "foodId")
        }
)
public class MealFood {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int mealId;              // Which meal this belongs to
    private int foodId;              // Which food was consumed
    private double servings;         // Number of servings (e.g., 1.5 servings)
    private double gramsConsumed;    // Actual grams eaten

    // Constructor
    public MealFood(int mealId, int foodId, double gramsConsumed) {
        this.mealId = mealId;
        this.foodId = foodId;
        this.gramsConsumed = gramsConsumed;
        this.servings = gramsConsumed / 100.0;  // Default: 100g per serving
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public double getGramsConsumed() {
        return gramsConsumed;
    }

    public void setGramsConsumed(double gramsConsumed) {
        this.gramsConsumed = gramsConsumed;
    }

    @Override
    public String toString() {
        return "MealFood{mealId=" + mealId +
                ", foodId=" + foodId +
                ", grams=" + gramsConsumed + "}";
    }
}