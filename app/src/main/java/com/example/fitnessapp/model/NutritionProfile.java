package com.example.fitnessapp.model;

import com.example.fitnessapp.algorithm.NutritionCalculator;

/**
 * Model class representing a user's complete nutrition profile.
 * Holds BMR, TDEE, calorie targets, and macronutrient distribution.
 */
public class NutritionProfile {

    private final double bmr;
    private final double tdee;
    private final double calorieTarget;
    private final double proteinGrams;
    private final double carbsGrams;
    private final double fatsGrams;
    private final double bmi;
    private final String bmiCategory;
    private final double waterIntakeLiters;
    private final NutritionCalculator.Goal goal;
    private final NutritionCalculator.ActivityLevel activityLevel;

    public NutritionProfile(double bmr,
                            double tdee,
                            double calorieTarget,
                            double proteinGrams,
                            double carbsGrams,
                            double fatsGrams,
                            double bmi,
                            String bmiCategory,
                            double waterIntakeLiters,
                            NutritionCalculator.Goal goal,
                            NutritionCalculator.ActivityLevel activityLevel) {
        this.bmr = bmr;
        this.tdee = tdee;
        this.calorieTarget = calorieTarget;
        this.proteinGrams = proteinGrams;
        this.carbsGrams = carbsGrams;
        this.fatsGrams = fatsGrams;
        this.bmi = bmi;
        this.bmiCategory = bmiCategory;
        this.waterIntakeLiters = waterIntakeLiters;
        this.goal = goal;
        this.activityLevel = activityLevel;
    }

    // Getters
    public double getBmr() {
        return bmr;
    }

    public double getTdee() {
        return tdee;
    }

    public double getCalorieTarget() {
        return calorieTarget;
    }

    public double getProteinGrams() {
        return proteinGrams;
    }

    public double getCarbsGrams() {
        return carbsGrams;
    }

    public double getFatsGrams() {
        return fatsGrams;
    }

    public double getBmi() {
        return bmi;
    }

    public String getBmiCategory() {
        return bmiCategory;
    }

    public double getWaterIntakeLiters() {
        return waterIntakeLiters;
    }

    public NutritionCalculator.Goal getGoal() {
        return goal;
    }

    public NutritionCalculator.ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    /**
     * Gets calorie surplus/deficit relative to TDEE.
     */
    public double getCalorieBalance() {
        return calorieTarget - tdee;
    }

    /**
     * Gets readable summary.
     */
    public String getSummary() {
        return String.format(
                "BMR: %.0f cal | TDEE: %.0f cal | Target: %.0f cal\n" +
                        "Macros: P=%.0fg C=%.0fg F=%.0fg\n" +
                        "BMI: %.1f (%s) | Water: %.1fL/day",
                bmr, tdee, calorieTarget,
                proteinGrams, carbsGrams, fatsGrams,
                bmi, bmiCategory, waterIntakeLiters
        );
    }

    @Override
    public String toString() {
        return "NutritionProfile{" +
                "bmr=" + bmr +
                ", tdee=" + tdee +
                ", calorieTarget=" + calorieTarget +
                ", protein=" + proteinGrams + "g" +
                ", carbs=" + carbsGrams + "g" +
                ", fats=" + fatsGrams + "g" +
                ", bmi=" + bmi +
                ", goal=" + goal +
                '}';
    }
}