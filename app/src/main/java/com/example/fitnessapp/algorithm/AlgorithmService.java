package com.example.fitnessapp.algorithm;

import com.example.fitnessapp.data.WeightEntry;
import com.example.fitnessapp.model.NutritionProfile;
import com.example.fitnessapp.model.WeightTrendAnalysis;
import com.example.fitnessapp.model.WorkoutMetrics;

import java.util.List;

/**
 * Service class that integrates all algorithm functionality.
 * Provides high-level methods for activities and ViewModels to use.
 *
 * This class serves as a facade pattern, simplifying the interface to
 * the complex algorithm subsystems.
 */
public class AlgorithmService {

    /**
     * Performs complete weight trend analysis.
     *
     * Time Complexity: O(n) where n is number of entries
     *
     * @param entries List of weight entries (sorted by date)
     * @param goalWeight User's goal weight
     * @return WeightTrendAnalysis object with all metrics
     */
    public static WeightTrendAnalysis analyzeWeightTrend(List<WeightEntry> entries, double goalWeight) {
        if (entries == null || entries.isEmpty()) {
            return null;
        }

        // Current weight
        double currentWeight = entries.get(entries.size() - 1).getWeight();

        // Moving averages
        List<Double> sevenDayAvg = StatisticalAnalyzer.calculateMovingAverage(entries, 7);
        List<Double> thirtyDayAvg = StatisticalAnalyzer.calculateMovingAverage(entries, 30);

        double sevenDayValue = sevenDayAvg.isEmpty() ? currentWeight :
                sevenDayAvg.get(sevenDayAvg.size() - 1);
        double thirtyDayValue = thirtyDayAvg.isEmpty() ? currentWeight :
                thirtyDayAvg.get(thirtyDayAvg.size() - 1);

        // Trend analysis
        double weeklyChangeRate = StatisticalAnalyzer.calculateWeightChangeRatePerWeek(entries);
        String trend = StatisticalAnalyzer.detectTrend(entries);

        // Predictions
        double predictedWeight = StatisticalAnalyzer.predictWeight(entries, 30);
        int daysToGoal = StatisticalAnalyzer.daysToGoal(entries, goalWeight);

        // Consistency metric
        double stdDev = StatisticalAnalyzer.calculateStandardDeviation(entries);

        return new WeightTrendAnalysis(
                currentWeight,
                sevenDayValue,
                thirtyDayValue,
                weeklyChangeRate,
                trend,
                predictedWeight,
                daysToGoal,
                stdDev
        );
    }

    /**
     * Calculates complete nutrition profile.
     *
     * Time Complexity: O(1)
     *
     * @param weightKg Current weight in kg
     * @param heightCm Height in cm
     * @param age Age in years
     * @param gender Gender (MALE/FEMALE)
     * @param activityLevel Activity level
     * @param goal Fitness goal (MAINTENANCE/CUTTING/BULKING)
     * @return Complete NutritionProfile
     */
    public static NutritionProfile calculateNutritionProfile(
            double weightKg,
            double heightCm,
            int age,
            NutritionCalculator.Gender gender,
            NutritionCalculator.ActivityLevel activityLevel,
            NutritionCalculator.Goal goal) {

        // Calculate BMR
        double bmr = NutritionCalculator.calculateBMR(weightKg, heightCm, age, gender);

        // Calculate TDEE
        double tdee = NutritionCalculator.calculateTDEE(bmr, activityLevel);

        // Calculate calorie target
        double calorieTarget = NutritionCalculator.calculateCalorieTarget(tdee, goal);
        calorieTarget = NutritionCalculator.validateCalorieTarget(calorieTarget, gender);

        // Calculate macros
        double[] macros = NutritionCalculator.calculateMacros(calorieTarget, goal);

        // Calculate BMI
        double bmi = NutritionCalculator.calculateBMI(weightKg, heightCm);
        String bmiCategory = NutritionCalculator.getBMICategory(bmi);

        // Calculate water intake
        double waterIntake = NutritionCalculator.calculateWaterIntake(weightKg, activityLevel);

        return new NutritionProfile(
                bmr,
                tdee,
                calorieTarget,
                macros[0], // protein
                macros[1], // carbs
                macros[2], // fats
                bmi,
                bmiCategory,
                waterIntake,
                goal,
                activityLevel
        );
    }

    /**
     * Analyzes a single workout set.
     *
     * Time Complexity: O(1)
     *
     * @param weight Weight lifted in kg
     * @param reps Reps performed
     * @param bodyWeight Body weight in kg
     * @param previousVolume Volume from previous workout (0 if first workout)
     * @return Complete WorkoutMetrics
     */
    public static WorkoutMetrics analyzeWorkoutSet(double weight, int reps,
                                                   double bodyWeight, double previousVolume) {
        // Calculate 1RM
        double oneRM = WorkoutAnalyzer.calculate1RMAverage(weight, reps);

        // Calculate volume
        double volume = WorkoutAnalyzer.calculateVolume(weight, reps);

        // Calculate intensity
        double percentOf1RM = WorkoutAnalyzer.calculatePercentOf1RM(weight, oneRM);

        // Determine strength level
        String strengthLevel = WorkoutAnalyzer.getStrengthLevel(oneRM, bodyWeight);

        // Check progressive overload
        boolean isProgressiveOverload = false;
        double volumeImprovement = 0.0;

        if (previousVolume > 0) {
            isProgressiveOverload = WorkoutAnalyzer.isProgressiveOverload(previousVolume, volume);
            volumeImprovement = WorkoutAnalyzer.calculateVolumeImprovement(previousVolume, volume);
        }

        return new WorkoutMetrics(
                weight,
                reps,
                oneRM,
                volume,
                percentOf1RM,
                strengthLevel,
                isProgressiveOverload,
                volumeImprovement
        );
    }

    /**
     * Generates sample data for testing/demo purposes.
     * Creates 30 days of realistic weight loss data.
     *
     * @param userId User ID for the entries
     * @return List of 30 WeightEntry objects
     */
    public static List<WeightEntry> generateSampleWeightData(int userId) {
        return SampleDataGenerator.generateRealistic30DayData(userId);
    }

    /**
     * Quick calculation: How many calories to eat for weight goal.
     * Simplified interface for common use case.
     *
     * @param currentWeightKg Current weight
     * @param heightCm Height
     * @param age Age
     * @param gender Gender
     * @param activityLevel Activity level
     * @param goalWeightKg Target weight
     * @return Daily calorie target
     */
    public static double calculateCaloriesForGoal(double currentWeightKg, double heightCm,
                                                  int age, NutritionCalculator.Gender gender,
                                                  NutritionCalculator.ActivityLevel activityLevel,
                                                  double goalWeightKg) {
        NutritionCalculator.Goal goal;

        if (goalWeightKg < currentWeightKg) {
            goal = NutritionCalculator.Goal.CUTTING;
        } else if (goalWeightKg > currentWeightKg) {
            goal = NutritionCalculator.Goal.BULKING;
        } else {
            goal = NutritionCalculator.Goal.MAINTENANCE;
        }

        NutritionProfile profile = calculateNutritionProfile(
                currentWeightKg, heightCm, age, gender, activityLevel, goal
        );

        return profile.getCalorieTarget();
    }
}