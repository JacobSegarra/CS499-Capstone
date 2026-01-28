package com.example.fitnessapp.algorithm;

/**
 * Calculates nutrition-related metrics using scientifically-validated formulas.
 * Includes BMR (Basal Metabolic Rate), TDEE (Total Daily Energy Expenditure),
 * and macronutrient distribution calculations.
 */
public class NutritionCalculator {

    /**
     * Gender enum for BMR calculations.
     */
    public enum Gender {
        MALE,
        FEMALE
    }

    /**
     * Activity level enum for TDEE calculations.
     */
    public enum ActivityLevel {
        SEDENTARY,      // Little/no exercise
        LIGHT,          // 1-3 days/week
        MODERATE,       // 3-5 days/week
        ACTIVE,         // 6-7 days/week
        VERY_ACTIVE     // Athlete/physical job
    }

    /**
     * Goal type for macro distribution.
     */
    public enum Goal {
        MAINTENANCE,    // Maintain current weight
        CUTTING,        // Lose fat (high protein)
        BULKING         // Gain muscle (high carbs)
    }

    /**
     * Calculates Basal Metabolic Rate using the Mifflin-St Jeor equation.
     * This is the most accurate formula for modern populations.
     *
     * Formula: BMR = (10 × weight_kg) + (6.25 × height_cm) - (5 × age) + s
     * where s = +5 for males, -161 for females
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     *
     * Source: Mifflin et al. (1990), Am J Clin Nutr
     *
     * @param weightKg Weight in kilograms
     * @param heightCm Height in centimeters
     * @param age Age in years
     * @param gender MALE or FEMALE
     * @return BMR in calories per day
     */
    public static double calculateBMR(double weightKg, double heightCm, int age, Gender gender) {
        double bmr = (FormulaConstants.MIFFLIN_WEIGHT_FACTOR * weightKg) +
                (FormulaConstants.MIFFLIN_HEIGHT_FACTOR * heightCm) -
                (FormulaConstants.MIFFLIN_AGE_FACTOR * age);

        if (gender == Gender.MALE) {
            bmr += FormulaConstants.MIFFLIN_MALE_CONSTANT;
        } else {
            bmr += FormulaConstants.MIFFLIN_FEMALE_CONSTANT;
        }

        return Math.round(bmr);
    }

    /**
     * Calculates Total Daily Energy Expenditure.
     * TDEE = BMR × Activity Multiplier
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     *
     * @param bmr Basal Metabolic Rate
     * @param activityLevel Activity level
     * @return TDEE in calories per day
     */
    public static double calculateTDEE(double bmr, ActivityLevel activityLevel) {
        double multiplier;

        switch (activityLevel) {
            case SEDENTARY:
                multiplier = FormulaConstants.ACTIVITY_SEDENTARY;
                break;
            case LIGHT:
                multiplier = FormulaConstants.ACTIVITY_LIGHT;
                break;
            case MODERATE:
                multiplier = FormulaConstants.ACTIVITY_MODERATE;
                break;
            case ACTIVE:
                multiplier = FormulaConstants.ACTIVITY_ACTIVE;
                break;
            case VERY_ACTIVE:
                multiplier = FormulaConstants.ACTIVITY_VERY_ACTIVE;
                break;
            default:
                multiplier = FormulaConstants.ACTIVITY_MODERATE;
        }

        return Math.round(bmr * multiplier);
    }

    /**
     * Convenience method: Calculate TDEE directly from body stats.
     *
     * @param weightKg Weight in kilograms
     * @param heightCm Height in centimeters
     * @param age Age in years
     * @param gender MALE or FEMALE
     * @param activityLevel Activity level
     * @return TDEE in calories per day
     */
    public static double calculateTDEE(double weightKg, double heightCm, int age,
                                       Gender gender, ActivityLevel activityLevel) {
        double bmr = calculateBMR(weightKg, heightCm, age, gender);
        return calculateTDEE(bmr, activityLevel);
    }

    /**
     * Calculates daily calorie target based on goal.
     *
     * Weight loss: TDEE - 500 (lose ~0.5kg/week)
     * Weight gain: TDEE + 300 (gain ~0.3kg/week)
     * Maintenance: TDEE
     *
     * Time Complexity: O(1)
     *
     * @param tdee Total Daily Energy Expenditure
     * @param goal Fitness goal
     * @return Target calories per day
     */
    public static double calculateCalorieTarget(double tdee, Goal goal) {
        double target = tdee;

        switch (goal) {
            case CUTTING:
                target = tdee - 500; // Moderate deficit
                break;
            case BULKING:
                target = tdee + 300; // Moderate surplus
                break;
            case MAINTENANCE:
            default:
                target = tdee;
        }

        return Math.round(target);
    }

    /**
     * Calculates macronutrient distribution in grams.
     * Returns array: [protein_g, carbs_g, fats_g]
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     *
     * @param totalCalories Daily calorie target
     * @param goal Fitness goal (affects macro ratios)
     * @return Array of [protein, carbs, fats] in grams
     */
    public static double[] calculateMacros(double totalCalories, Goal goal) {
        double proteinPercent, carbsPercent, fatsPercent;

        switch (goal) {
            case CUTTING:
                proteinPercent = FormulaConstants.MACRO_CUTTING_PROTEIN;
                carbsPercent = FormulaConstants.MACRO_CUTTING_CARBS;
                fatsPercent = FormulaConstants.MACRO_CUTTING_FATS;
                break;
            case BULKING:
                proteinPercent = FormulaConstants.MACRO_BULKING_PROTEIN;
                carbsPercent = FormulaConstants.MACRO_BULKING_CARBS;
                fatsPercent = FormulaConstants.MACRO_BULKING_FATS;
                break;
            case MAINTENANCE:
            default:
                proteinPercent = FormulaConstants.MACRO_BALANCED_PROTEIN;
                carbsPercent = FormulaConstants.MACRO_BALANCED_CARBS;
                fatsPercent = FormulaConstants.MACRO_BALANCED_FATS;
        }

        // Calculate calories for each macro
        double proteinCalories = totalCalories * proteinPercent;
        double carbsCalories = totalCalories * carbsPercent;
        double fatsCalories = totalCalories * fatsPercent;

        // Convert to grams
        double proteinGrams = proteinCalories / FormulaConstants.CALORIES_PER_GRAM_PROTEIN;
        double carbsGrams = carbsCalories / FormulaConstants.CALORIES_PER_GRAM_CARBS;
        double fatsGrams = fatsCalories / FormulaConstants.CALORIES_PER_GRAM_FATS;

        return new double[]{
                Math.round(proteinGrams),
                Math.round(carbsGrams),
                Math.round(fatsGrams)
        };
    }

    /**
     * Calculates protein requirement based on body weight.
     * General guideline: 1.6-2.2g per kg for active individuals.
     *
     * Time Complexity: O(1)
     *
     * @param weightKg Body weight in kg
     * @param goal Fitness goal
     * @return Recommended protein in grams per day
     */
    public static double calculateProteinRequirement(double weightKg, Goal goal) {
        double multiplier;

        switch (goal) {
            case CUTTING:
                multiplier = 2.2; // Higher protein to preserve muscle during deficit
                break;
            case BULKING:
                multiplier = 1.8; // Moderate protein for muscle growth
                break;
            case MAINTENANCE:
            default:
                multiplier = 1.6; // Baseline for active individuals
        }

        return Math.round(weightKg * multiplier);
    }

    /**
     * Calculates BMI (Body Mass Index).
     * BMI = weight(kg) / height(m)²
     *
     * Time Complexity: O(1)
     *
     * @param weightKg Weight in kilograms
     * @param heightCm Height in centimeters
     * @return BMI value
     */
    public static double calculateBMI(double weightKg, double heightCm) {
        double heightMeters = heightCm / 100.0;
        double bmi = weightKg / (heightMeters * heightMeters);
        return Math.round(bmi * 10.0) / 10.0;
    }

    /**
     * Gets BMI category as a string.
     *
     * Time Complexity: O(1)
     *
     * @param bmi BMI value
     * @return Category: "Underweight", "Normal", "Overweight", or "Obese"
     */
    public static String getBMICategory(double bmi) {
        if (bmi < FormulaConstants.BMI_UNDERWEIGHT_THRESHOLD) {
            return "Underweight";
        } else if (bmi <= FormulaConstants.BMI_NORMAL_THRESHOLD) {
            return "Normal";
        } else if (bmi <= FormulaConstants.BMI_OVERWEIGHT_THRESHOLD) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    /**
     * Calculates water intake recommendation.
     * General guideline: 30-35ml per kg of body weight.
     *
     * Time Complexity: O(1)
     *
     * @param weightKg Body weight in kg
     * @param activityLevel Activity level
     * @return Recommended water intake in liters per day
     */
    public static double calculateWaterIntake(double weightKg, ActivityLevel activityLevel) {
        double baseIntake = weightKg * 0.033; // 33ml per kg (base)

        // Adjust for activity level
        if (activityLevel == ActivityLevel.ACTIVE || activityLevel == ActivityLevel.VERY_ACTIVE) {
            baseIntake *= 1.15; // Add 15% for active individuals
        }

        return Math.round(baseIntake * 10.0) / 10.0;
    }

    /**
     * Validates that calorie target is safe.
     * Returns adjusted value if below minimum safe threshold.
     *
     * @param calorieTarget Calculated calorie target
     * @param gender User's gender
     * @return Safe calorie target (minimum enforced)
     */
    public static double validateCalorieTarget(double calorieTarget, Gender gender) {
        int minCalories = (gender == Gender.MALE) ?
                FormulaConstants.MIN_CALORIES_MALE :
                FormulaConstants.MIN_CALORIES_FEMALE;

        if (calorieTarget < minCalories) {
            return minCalories; // Enforce safety floor
        }

        return calorieTarget;
    }
}