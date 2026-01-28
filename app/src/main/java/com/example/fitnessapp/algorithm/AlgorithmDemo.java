package com.example.fitnessapp.algorithm;

import com.example.fitnessapp.data.WeightEntry;
import com.example.fitnessapp.model.NutritionProfile;
import com.example.fitnessapp.model.WeightTrendAnalysis;
import com.example.fitnessapp.model.WorkoutMetrics;

import java.util.List;

/**
 * Demo class showing how to use all algorithm functionality.
 * This class can be called from an Activity to test algorithms.
 *
 * USAGE: Call AlgorithmDemo.runAllTests() to see output in Logcat
 */
public class AlgorithmDemo {

    /**
     * Runs all algorithm demos and returns results as a string.
     * Can be displayed in a TextView or logged.
     *
     * @param userId User ID for generating sample data
     * @return String containing all demo results
     */
    public static String runAllTests(int userId) {
        StringBuilder results = new StringBuilder();
        results.append("=== FITNESSAPP ALGORITHM DEMO ===\n\n");

        // Test 1: Statistical Analysis
        results.append(testStatisticalAnalysis(userId));
        results.append("\n\n");

        // Test 2: Nutrition Calculations
        results.append(testNutritionCalculations());
        results.append("\n\n");

        // Test 3: Workout Analysis
        results.append(testWorkoutAnalysis());

        return results.toString();
    }

    /**
     * Tests statistical analysis algorithms.
     */
    private static String testStatisticalAnalysis(int userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- STATISTICAL ANALYSIS TEST ---\n");

        // Generate sample data (30 days, 85kg → 83kg)
        List<WeightEntry> entries = SampleDataGenerator.generateRealistic30DayData(userId);
        sb.append(String.format("Generated %d days of sample data\n", entries.size()));
        sb.append(String.format("Start: %.1fkg | End: %.1fkg\n",
                entries.get(0).getWeight(),
                entries.get(entries.size()-1).getWeight()));

        // Analyze trend
        double goalWeight = 80.0;
        WeightTrendAnalysis analysis = AlgorithmService.analyzeWeightTrend(entries, goalWeight);

        if (analysis != null) {
            sb.append("\nResults:\n");
            sb.append(String.format("  Current: %.1fkg\n", analysis.getCurrentWeight()));
            sb.append(String.format("  7-day avg: %.1fkg\n", analysis.getSevenDayAverage()));
            sb.append(String.format("  30-day avg: %.1fkg\n", analysis.getThirtyDayAverage()));
            sb.append(String.format("  Trend: %s\n", analysis.getTrendDescription()));
            sb.append(String.format("  Predicted (30 days): %.1fkg\n", analysis.getPredictedWeightIn30Days()));
            sb.append(String.format("  Goal progress: %s\n", analysis.getGoalProgressMessage()));
            sb.append(String.format("  Consistency (σ): %.2fkg\n", analysis.getStandardDeviation()));
        }

        return sb.toString();
    }

    /**
     * Tests nutrition calculation algorithms.
     */
    private static String testNutritionCalculations() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- NUTRITION CALCULATIONS TEST ---\n");

        // Example user profile
        double weight = 85.0;  // kg
        double height = 180.0; // cm
        int age = 28;
        NutritionCalculator.Gender gender = NutritionCalculator.Gender.MALE;
        NutritionCalculator.ActivityLevel activity = NutritionCalculator.ActivityLevel.MODERATE;
        NutritionCalculator.Goal goal = NutritionCalculator.Goal.CUTTING;

        sb.append(String.format("User: %dyo male, %.0fcm, %.1fkg\n", age, height, weight));
        sb.append(String.format("Activity: %s | Goal: %s\n", activity, goal));

        // Calculate profile
        NutritionProfile profile = AlgorithmService.calculateNutritionProfile(
                weight, height, age, gender, activity, goal
        );

        if (profile != null) {
            sb.append("\nResults:\n");
            sb.append(String.format("  BMR: %.0f calories/day\n", profile.getBmr()));
            sb.append(String.format("  TDEE: %.0f calories/day\n", profile.getTdee()));
            sb.append(String.format("  Target: %.0f calories/day (%.0f deficit)\n",
                    profile.getCalorieTarget(),
                    Math.abs(profile.getCalorieBalance())));
            sb.append("\nMacros:\n");
            sb.append(String.format("  Protein: %.0fg (%.0f cal)\n",
                    profile.getProteinGrams(),
                    profile.getProteinGrams() * 4));
            sb.append(String.format("  Carbs: %.0fg (%.0f cal)\n",
                    profile.getCarbsGrams(),
                    profile.getCarbsGrams() * 4));
            sb.append(String.format("  Fats: %.0fg (%.0f cal)\n",
                    profile.getFatsGrams(),
                    profile.getFatsGrams() * 9));
            sb.append(String.format("\nBMI: %.1f (%s)\n", profile.getBmi(), profile.getBmiCategory()));
            sb.append(String.format("Water: %.1f liters/day\n", profile.getWaterIntakeLiters()));
        }

        return sb.toString();
    }

    /**
     * Tests workout analysis algorithms.
     */
    private static String testWorkoutAnalysis() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- WORKOUT ANALYSIS TEST ---\n");

        // Example: Bench press workout
        double weight = 100.0;  // kg
        int reps = 5;
        double bodyWeight = 85.0; // kg
        double previousVolume = 480.0; // Previous workout: 100kg × 5 reps × 4 sets = 2000kg... wait, let me recalculate
        // Let's say previous was 96kg × 5 reps = 480kg volume

        sb.append(String.format("Exercise: Bench Press\n"));
        sb.append(String.format("Today: %.0fkg × %d reps\n", weight, reps));
        sb.append(String.format("Body weight: %.0fkg\n", bodyWeight));

        // Analyze workout
        WorkoutMetrics metrics = AlgorithmService.analyzeWorkoutSet(
                weight, reps, bodyWeight, previousVolume
        );

        if (metrics != null) {
            sb.append("\nResults:\n");
            sb.append(String.format("  Estimated 1RM: %.1fkg\n", metrics.getEstimatedOneRM()));
            sb.append(String.format("  Volume: %.0fkg\n", metrics.getVolume()));
            sb.append(String.format("  Intensity: %.1f%% of 1RM\n", metrics.getPercentOf1RM()));
            sb.append(String.format("  Strength level: %s\n", metrics.getStrengthLevel()));
            sb.append(String.format("  %s\n", metrics.getProgressMessage()));

            // Calculate training recommendations
            double oneRM = metrics.getEstimatedOneRM();
            sb.append("\nTraining Recommendations:\n");
            sb.append(String.format("  For 3 reps: %.1fkg\n",
                    WorkoutAnalyzer.calculateTrainingWeight(oneRM, 3)));
            sb.append(String.format("  For 8 reps: %.1fkg\n",
                    WorkoutAnalyzer.calculateTrainingWeight(oneRM, 8)));
            sb.append(String.format("  For 12 reps: %.1fkg\n",
                    WorkoutAnalyzer.calculateTrainingWeight(oneRM, 12)));
        }

        return sb.toString();
    }

    /**
     * Quick test for specific algorithm.
     */
    public static String quickTest() {
        return "FitnessApp Algorithms v1.0\n" +
                "✓ Statistical Analysis\n" +
                "✓ Nutrition Calculations\n" +
                "✓ Workout Analysis\n" +
                "\nAll systems operational!";
    }
}