package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity storing user's personalized nutrition goals.
 * Calculated using NutritionCalculator algorithms (BMR, TDEE, macros).
 * One record per user (updated when settings change).
 */
@Entity(
        tableName = "nutrition_goals",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "userId", unique = true)  // One goal per user
        }
)
public class NutritionGoal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;

    // User profile data (for calculations)
    private double height;           // cm
    private int age;
    private String gender;           // 'MALE' or 'FEMALE'
    private String activityLevel;    // 'SEDENTARY', 'LIGHT', 'MODERATE', 'ACTIVE', 'VERY_ACTIVE'
    private String goal;             // 'CUTTING', 'BULKING', 'MAINTENANCE'

    // Calculated values (from NutritionCalculator)
    private double bmr;              // Basal Metabolic Rate
    private double tdee;             // Total Daily Energy Expenditure
    private double calorieTarget;    // Daily calorie goal
    private double proteinTarget;    // Grams per day
    private double carbsTarget;      // Grams per day
    private double fatsTarget;       // Grams per day
    private double waterTarget;      // Liters per day

    private long calculatedAt;       // Timestamp when calculated

    // Constructor
    public NutritionGoal(int userId, double height, int age, String gender,
                         String activityLevel, String goal) {
        this.userId = userId;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.calculatedAt = System.currentTimeMillis();
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getTdee() {
        return tdee;
    }

    public void setTdee(double tdee) {
        this.tdee = tdee;
    }

    public double getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(double calorieTarget) {
        this.calorieTarget = calorieTarget;
    }

    public double getProteinTarget() {
        return proteinTarget;
    }

    public void setProteinTarget(double proteinTarget) {
        this.proteinTarget = proteinTarget;
    }

    public double getCarbsTarget() {
        return carbsTarget;
    }

    public void setCarbsTarget(double carbsTarget) {
        this.carbsTarget = carbsTarget;
    }

    public double getFatsTarget() {
        return fatsTarget;
    }

    public void setFatsTarget(double fatsTarget) {
        this.fatsTarget = fatsTarget;
    }

    public double getWaterTarget() {
        return waterTarget;
    }

    public void setWaterTarget(double waterTarget) {
        this.waterTarget = waterTarget;
    }

    public long getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(long calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    /**
     * Calculate calorie deficit/surplus.
     */
    public double getCalorieBalance() {
        return calorieTarget - tdee;
    }

    /**
     * Get formatted goals summary.
     */
    public String getSummary() {
        return String.format("Target: %.0f cal | P: %.0fg | C: %.0fg | F: %.0fg",
                calorieTarget, proteinTarget, carbsTarget, fatsTarget);
    }

    /**
     * Check if goals need recalculation (older than 30 days).
     */
    public boolean needsRecalculation() {
        long thirtyDaysMs = 30L * 24 * 60 * 60 * 1000;
        return (System.currentTimeMillis() - calculatedAt) > thirtyDaysMs;
    }

    @Override
    public String toString() {
        return "NutritionGoal{" + goal + ", " +
                String.format("%.0f cal/day", calorieTarget) +
                ", BMR: " + String.format("%.0f", bmr) + "}";
    }
}