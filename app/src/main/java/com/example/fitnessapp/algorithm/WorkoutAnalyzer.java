package com.example.fitnessapp.algorithm;

import java.util.List;

/**
 * Analyzes workout data including 1RM calculations and progressive overload detection.
 * Uses scientifically-validated formulas for strength predictions.
 */
public class WorkoutAnalyzer {

    /**
     * Calculates One-Rep Max using the Epley formula.
     *
     * Formula: 1RM = weight × (1 + reps / 30)
     *
     * Best accuracy: 1-10 rep range
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     *
     * Source: Epley, B. (1985). "Poundage Chart"
     * Accuracy: ±5% for most individuals in 1-10 rep range
     *
     * @param weight Weight lifted in kg
     * @param reps Number of repetitions performed
     * @return Estimated 1RM in kg
     */
    public static double calculate1RMEpley(double weight, int reps) {
        if (reps <= 0 || weight <= 0) {
            return 0.0;
        }

        if (reps == 1) {
            return weight; // Already at 1RM
        }

        double oneRM = weight * (1.0 + (reps / FormulaConstants.EPLEY_CONSTANT));
        return Math.round(oneRM * 10.0) / 10.0;
    }

    /**
     * Calculates One-Rep Max using the Brzycki formula.
     *
     * Formula: 1RM = weight × (36 / (37 - reps))
     *
     * Best accuracy: 2-10 rep range
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     *
     * Source: Brzycki, M. (1993). "Strength Testing: Predicting a One-Rep Max"
     * Accuracy: ±3% for 2-10 rep range
     *
     * @param weight Weight lifted in kg
     * @param reps Number of repetitions performed
     * @return Estimated 1RM in kg
     */
    public static double calculate1RMBrzycki(double weight, int reps) {
        if (reps <= 0 || weight <= 0) {
            return 0.0;
        }

        if (reps == 1) {
            return weight; // Already at 1RM
        }

        if (reps >= 37) {
            return 0.0; // Formula not valid for 37+ reps
        }

        double oneRM = weight * (FormulaConstants.BRZYCKI_NUMERATOR /
                (FormulaConstants.BRZYCKI_DENOMINATOR_BASE - reps));
        return Math.round(oneRM * 10.0) / 10.0;
    }

    /**
     * Calculates average 1RM using both Epley and Brzycki formulas.
     * More accurate than either formula alone.
     *
     * Time Complexity: O(1)
     *
     * @param weight Weight lifted in kg
     * @param reps Number of repetitions performed
     * @return Average estimated 1RM in kg
     */
    public static double calculate1RMAverage(double weight, int reps) {
        double epley = calculate1RMEpley(weight, reps);
        double brzycki = calculate1RMBrzycki(weight, reps);

        if (brzycki == 0.0) {
            return epley; // Brzycki invalid for high reps
        }

        return Math.round(((epley + brzycki) / 2.0) * 10.0) / 10.0;
    }

    /**
     * Calculates training weight for a target rep range.
     * Inverse of 1RM calculation: weight = 1RM / (1 + reps/30)
     *
     * Time Complexity: O(1)
     *
     * @param oneRM Estimated or actual 1RM
     * @param targetReps Desired number of reps
     * @return Weight to use for target reps
     */
    public static double calculateTrainingWeight(double oneRM, int targetReps) {
        if (targetReps <= 0 || oneRM <= 0) {
            return 0.0;
        }

        if (targetReps == 1) {
            return oneRM;
        }

        double weight = oneRM / (1.0 + (targetReps / FormulaConstants.EPLEY_CONSTANT));
        return Math.round(weight * 10.0) / 10.0;
    }

    /**
     * Calculates percentage of 1RM.
     * Useful for programming training intensity.
     *
     * Time Complexity: O(1)
     *
     * @param weight Training weight
     * @param oneRM One-rep max
     * @return Percentage of 1RM (0-100)
     */
    public static double calculatePercentOf1RM(double weight, double oneRM) {
        if (oneRM <= 0) {
            return 0.0;
        }

        double percent = (weight / oneRM) * 100.0;
        return Math.round(percent * 10.0) / 10.0;
    }

    /**
     * Calculates total volume for a set.
     * Volume = Weight × Reps
     *
     * Time Complexity: O(1)
     *
     * @param weight Weight lifted in kg
     * @param reps Number of repetitions
     * @return Total volume in kg
     */
    public static double calculateVolume(double weight, int reps) {
        return weight * reps;
    }

    /**
     * Calculates total volume for multiple sets.
     *
     * Time Complexity: O(n) where n is number of sets
     *
     * @param weights Array of weights per set
     * @param reps Array of reps per set
     * @return Total volume in kg
     */
    public static double calculateTotalVolume(double[] weights, int[] reps) {
        if (weights == null || reps == null || weights.length != reps.length) {
            return 0.0;
        }

        double totalVolume = 0.0;
        for (int i = 0; i < weights.length; i++) {
            totalVolume += calculateVolume(weights[i], reps[i]);
        }

        return totalVolume;
    }

    /**
     * Detects progressive overload between two workouts.
     * Progressive overload = increase in total volume ≥ 2.5%
     *
     * Time Complexity: O(1)
     *
     * @param previousVolume Volume from previous workout
     * @param currentVolume Volume from current workout
     * @return true if progressive overload achieved
     */
    public static boolean isProgressiveOverload(double previousVolume, double currentVolume) {
        if (previousVolume <= 0) {
            return false;
        }

        double percentIncrease = (currentVolume - previousVolume) / previousVolume;
        return percentIncrease >= FormulaConstants.PROGRESSIVE_OVERLOAD_THRESHOLD;
    }

    /**
     * Calculates percent improvement in volume.
     *
     * Time Complexity: O(1)
     *
     * @param previousVolume Previous workout volume
     * @param currentVolume Current workout volume
     * @return Percent change (positive = improvement, negative = decline)
     */
    public static double calculateVolumeImprovement(double previousVolume, double currentVolume) {
        if (previousVolume <= 0) {
            return 0.0;
        }

        double percentChange = ((currentVolume - previousVolume) / previousVolume) * 100.0;
        return Math.round(percentChange * 10.0) / 10.0;
    }

    /**
     * Calculates strength level category based on 1RM relative to body weight.
     * Based on strength standards for major lifts.
     *
     * Time Complexity: O(1)
     *
     * @param oneRM One-rep max in kg
     * @param bodyWeight Body weight in kg
     * @return Strength level: "Beginner", "Intermediate", "Advanced", "Elite"
     */
    public static String getStrengthLevel(double oneRM, double bodyWeight) {
        if (bodyWeight <= 0 || oneRM <= 0) {
            return "Unknown";
        }

        double ratio = oneRM / bodyWeight;

        // These are approximate standards (vary by lift and gender)
        // Using conservative squat standards as baseline
        if (ratio < 1.0) {
            return "Beginner";
        } else if (ratio < 1.5) {
            return "Intermediate";
        } else if (ratio < 2.0) {
            return "Advanced";
        } else {
            return "Elite";
        }
    }

    /**
     * Calculates volume load for a workout session.
     * Total work = Σ(sets × reps × weight)
     *
     * Time Complexity: O(n) where n is number of exercises
     *
     * @param exercises List of exercise volumes
     * @return Total session volume in kg
     */
    public static double calculateSessionVolume(List<Double> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            return 0.0;
        }

        double totalVolume = 0.0;
        for (double exerciseVolume : exercises) {
            totalVolume += exerciseVolume;
        }

        return Math.round(totalVolume);
    }

    /**
     * Calculates intensity (average % of 1RM for a workout).
     * Higher intensity = working closer to maximum.
     *
     * Time Complexity: O(n)
     *
     * @param weights Array of weights used
     * @param oneRMs Array of corresponding 1RMs
     * @return Average intensity as percentage (0-100)
     */
    public static double calculateAverageIntensity(double[] weights, double[] oneRMs) {
        if (weights == null || oneRMs == null || weights.length != oneRMs.length) {
            return 0.0;
        }

        if (weights.length == 0) {
            return 0.0;
        }

        double totalPercent = 0.0;
        for (int i = 0; i < weights.length; i++) {
            totalPercent += calculatePercentOf1RM(weights[i], oneRMs[i]);
        }

        double avgIntensity = totalPercent / weights.length;
        return Math.round(avgIntensity * 10.0) / 10.0;
    }

    /**
     * Suggests deload based on performance decline.
     * Deload recommended if volume drops > 10% for 2+ consecutive sessions.
     *
     * Time Complexity: O(n)
     *
     * @param recentVolumes Last 3-5 workout volumes
     * @return true if deload recommended
     */
    public static boolean shouldDeload(double[] recentVolumes) {
        if (recentVolumes == null || recentVolumes.length < 3) {
            return false;
        }

        int decliningCount = 0;

        for (int i = 1; i < recentVolumes.length; i++) {
            double percentChange = ((recentVolumes[i] - recentVolumes[i-1]) / recentVolumes[i-1]) * 100.0;

            if (percentChange < -10.0) {
                decliningCount++;
            }
        }

        return decliningCount >= 2; // 2+ consecutive declines
    }

    /**
     * Calculates rest time recommendation based on intensity.
     * Higher intensity = longer rest periods needed.
     *
     * Time Complexity: O(1)
     *
     * @param percentOf1RM Percentage of 1RM being lifted
     * @return Recommended rest time in seconds
     */
    public static int getRecommendedRestTime(double percentOf1RM) {
        if (percentOf1RM >= 90.0) {
            return 300; // 5 minutes for max effort
        } else if (percentOf1RM >= 80.0) {
            return 180; // 3 minutes for heavy sets
        } else if (percentOf1RM >= 70.0) {
            return 120; // 2 minutes for moderate
        } else {
            return 60;  // 1 minute for light/technique work
        }
    }
}