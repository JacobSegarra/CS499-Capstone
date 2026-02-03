package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a food item in the database.
 * Nutritional values are per 100g by default.
 */
@Entity(
        tableName = "foods",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "name"),
                @Index(value = "category"),
                @Index(value = "userId")
        }
)
public class Food {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String brand;
    private double servingSize;      // Grams (default 100.0)
    private double calories;
    private double protein;          // Grams
    private double carbs;            // Grams
    private double fats;             // Grams
    private double fiber;
    private double sugar;
    private String category;         // 'protein', 'vegetable', 'grain', 'fruit', 'dairy', 'snack'
    private String barcode;          // For future barcode scanning
    private boolean isCustom;        // false = database food, true = user-created
    private Integer userId;          // NULL for database foods, user ID for custom foods
    private long createdAt;

    // Constructor
    public Food(String name, double calories, double protein, double carbs, double fats) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.servingSize = 100.0;
        this.fiber = 0.0;
        this.sugar = 0.0;
        this.isCustom = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Helper method: Calculate nutrition for a specific serving.
     */
    public double[] calculateForServing(double grams) {
        double ratio = grams / servingSize;
        return new double[]{
                calories * ratio,
                protein * ratio,
                carbs * ratio,
                fats * ratio
        };
    }

    @Override
    public String toString() {
        return "Food{name='" + name + "', cal=" + calories + ", P=" + protein + "g, C=" + carbs + "g, F=" + fats + "g}";
    }
}