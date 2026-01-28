package com.example.fitnessapp.algorithm;

/**
 * Contains scientifically-validated formulas and constants for fitness calculations.
 * All formulas are documented with sources and validation studies.
 */
public class FormulaConstants {

    // ===== NUTRITION FORMULAS =====

    /**
     * Mifflin-St Jeor equation constants for BMR calculation.
     * Formula: BMR = (10 × weight_kg) + (6.25 × height_cm) - (5 × age) + s
     * where s = +5 for males, -161 for females
     * Source: Mifflin et al. (1990). "A new predictive equation for resting energy
     * expenditure in healthy individuals." The American Journal of Clinical Nutrition.
     * Accuracy: ±10% for most individuals
     */
    public static final double MIFFLIN_WEIGHT_FACTOR = 10.0;
    public static final double MIFFLIN_HEIGHT_FACTOR = 6.25;
    public static final double MIFFLIN_AGE_FACTOR = 5.0;
    public static final double MIFFLIN_MALE_CONSTANT = 5.0;
    public static final double MIFFLIN_FEMALE_CONSTANT = -161.0;

    /**
     * Activity multipliers for TDEE calculation.
     * TDEE = BMR × Activity Factor
     * Source: Harris-Benedict equation refinements (Roza & Shizgal, 1984)
     */
    public static final double ACTIVITY_SEDENTARY = 1.2;        // Little/no exercise
    public static final double ACTIVITY_LIGHT = 1.375;          // 1-3 days/week
    public static final double ACTIVITY_MODERATE = 1.55;        // 3-5 days/week
    public static final double ACTIVITY_ACTIVE = 1.725;         // 6-7 days/week
    public static final double ACTIVITY_VERY_ACTIVE = 1.9;      // Athlete/physical job

    /**
     * Macro distribution constants (percentage of total calories).
     * These are general guidelines; individual needs vary.
     * Source: USDA Dietary Guidelines & ISSN Position Stand (2017)
     */
    // Balanced diet (maintenance)
    public static final double MACRO_BALANCED_PROTEIN = 0.30;   // 30% protein
    public static final double MACRO_BALANCED_CARBS = 0.40;     // 40% carbs
    public static final double MACRO_BALANCED_FATS = 0.30;      // 30% fats

    // Weight loss (high protein)
    public static final double MACRO_CUTTING_PROTEIN = 0.40;    // 40% protein
    public static final double MACRO_CUTTING_CARBS = 0.30;      // 30% carbs
    public static final double MACRO_CUTTING_FATS = 0.30;       // 30% fats

    // Muscle gain (high carb)
    public static final double MACRO_BULKING_PROTEIN = 0.30;    // 30% protein
    public static final double MACRO_BULKING_CARBS = 0.50;      // 50% carbs
    public static final double MACRO_BULKING_FATS = 0.20;       // 20% fats

    /**
     * Calorie content per gram of macronutrient.
     */
    public static final double CALORIES_PER_GRAM_PROTEIN = 4.0;
    public static final double CALORIES_PER_GRAM_CARBS = 4.0;
    public static final double CALORIES_PER_GRAM_FATS = 9.0;

    // ===== WORKOUT FORMULAS =====

    /**
     * Epley formula for 1RM calculation.
     * Formula: 1RM = weight × (1 + reps / 30)
     * Source: Epley, B. (1985). "Poundage Chart"
     * Best accuracy: 1-10 reps range
     */
    public static final double EPLEY_CONSTANT = 30.0;

    /**
     * Brzycki formula for 1RM calculation.
     * Formula: 1RM = weight × (36 / (37 - reps))
     * Source: Brzycki, M. (1993). "Strength Testing: Predicting a One-Rep Max"
     * Best accuracy: 2-10 reps range
     */
    public static final double BRZYCKI_NUMERATOR = 36.0;
    public static final double BRZYCKI_DENOMINATOR_BASE = 37.0;

    /**
     * Progressive overload threshold.
     * An increase of 2.5% or more in total volume is considered progressive overload.
     */
    public static final double PROGRESSIVE_OVERLOAD_THRESHOLD = 0.025; // 2.5%

    // ===== STATISTICAL ANALYSIS =====

    /**
     * Minimum data points required for meaningful statistical analysis.
     */
    public static final int MIN_DATA_POINTS_FOR_TREND = 7;      // 1 week minimum
    public static final int MIN_DATA_POINTS_FOR_PREDICTION = 14; // 2 weeks minimum

    /**
     * Moving average window sizes.
     */
    public static final int MOVING_AVERAGE_SHORT_WINDOW = 7;    // 1 week
    public static final int MOVING_AVERAGE_LONG_WINDOW = 30;    // 1 month

    /**
     * Trend detection thresholds (kg per week).
     * Changes smaller than this are considered maintenance.
     */
    public static final double TREND_THRESHOLD_KG_PER_WEEK = 0.2; // 0.2 kg/week

    // ===== BMI CATEGORIES =====

    /**
     * BMI category thresholds.
     * Source: World Health Organization (WHO)
     */
    public static final double BMI_UNDERWEIGHT_THRESHOLD = 18.5;
    public static final double BMI_NORMAL_THRESHOLD = 24.9;
    public static final double BMI_OVERWEIGHT_THRESHOLD = 29.9;
    // Above 30.0 = Obese

    // ===== SAFETY LIMITS =====

    /**
     * Recommended weight loss/gain rates (kg per week).
     * Source: CDC guidelines for safe weight change
     */
    public static final double SAFE_WEIGHT_LOSS_MAX_KG_PER_WEEK = 1.0;
    public static final double SAFE_WEIGHT_GAIN_MAX_KG_PER_WEEK = 0.5;

    /**
     * Minimum calorie intake (safety floor).
     * Going below this is generally not recommended without medical supervision.
     */
    public static final int MIN_CALORIES_MALE = 1500;
    public static final int MIN_CALORIES_FEMALE = 1200;
}