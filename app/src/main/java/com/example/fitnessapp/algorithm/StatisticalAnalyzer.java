package com.example.fitnessapp.algorithm;

import com.example.fitnessapp.data.WeightEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides statistical analysis algorithms for weight data.
 * Includes moving averages, linear regression, and trend detection.
 */
public class StatisticalAnalyzer {

    /**
     * Calculates simple moving average for weight data.
     *
     * Time Complexity: O(n) where n is the number of entries
     * Space Complexity: O(n) for storing averages
     *
     * @param entries List of weight entries (must be sorted by date)
     * @param windowSize Number of days in the moving average window
     * @return List of averaged weights
     */
    public static List<Double> calculateMovingAverage(List<WeightEntry> entries, int windowSize) {
        List<Double> averages = new ArrayList<>();

        if (entries == null || entries.size() < windowSize) {
            return averages; // Not enough data
        }

        for (int i = 0; i <= entries.size() - windowSize; i++) {
            double sum = 0.0;

            // Sum weights in the window
            for (int j = i; j < i + windowSize; j++) {
                sum += entries.get(j).getWeight();
            }

            double average = sum / windowSize;
            averages.add(Math.round(average * 10.0) / 10.0); // Round to 1 decimal
        }

        return averages;
    }

    /**
     * Calculates weight change rate using linear regression.
     * Returns the slope (kg per day).
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * Uses least squares method: slope = (n*Σxy - Σx*Σy) / (n*Σx² - (Σx)²)
     *
     * @param entries List of weight entries (must be sorted by date)
     * @return Weight change rate in kg per day (negative = losing, positive = gaining)
     */
    public static double calculateWeightChangeRate(List<WeightEntry> entries) {
        if (entries == null || entries.size() < 2) {
            return 0.0;
        }

        int n = entries.size();
        long startTime = entries.get(0).getTimestamp();

        double sumX = 0.0;    // Sum of days
        double sumY = 0.0;    // Sum of weights
        double sumXY = 0.0;   // Sum of (days * weight)
        double sumX2 = 0.0;   // Sum of (days²)

        for (int i = 0; i < n; i++) {
            // Convert timestamp to days since start
            double x = (entries.get(i).getTimestamp() - startTime) / (1000.0 * 60 * 60 * 24);
            double y = entries.get(i).getWeight();

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        // Calculate slope using least squares formula
        double denominator = (n * sumX2 - sumX * sumX);
        if (Math.abs(denominator) < 0.0001) {
            return 0.0; // Avoid division by zero
        }

        double slope = (n * sumXY - sumX * sumY) / denominator;

        return Math.round(slope * 1000.0) / 1000.0; // Round to 3 decimals
    }

    /**
     * Calculates weight change rate in kg per week (more user-friendly).
     *
     * @param entries List of weight entries
     * @return Weight change rate in kg per week
     */
    public static double calculateWeightChangeRatePerWeek(List<WeightEntry> entries) {
        return calculateWeightChangeRate(entries) * 7.0; // Convert days to weeks
    }

    /**
     * Detects the overall weight trend.
     *
     * Time Complexity: O(n)
     *
     * @param entries List of weight entries
     * @return "LOSING", "GAINING", or "MAINTAINING"
     */
    public static String detectTrend(List<WeightEntry> entries) {
        if (entries == null || entries.size() < FormulaConstants.MIN_DATA_POINTS_FOR_TREND) {
            return "INSUFFICIENT_DATA";
        }

        double ratePerWeek = calculateWeightChangeRatePerWeek(entries);

        if (ratePerWeek < -FormulaConstants.TREND_THRESHOLD_KG_PER_WEEK) {
            return "LOSING";
        } else if (ratePerWeek > FormulaConstants.TREND_THRESHOLD_KG_PER_WEEK) {
            return "GAINING";
        } else {
            return "MAINTAINING";
        }
    }

    /**
     * Predicts future weight based on current trend.
     * Uses linear regression to project forward.
     *
     * Time Complexity: O(n)
     *
     * @param entries List of weight entries
     * @param daysAhead Number of days to predict forward
     * @return Predicted weight in kg
     */
    public static double predictWeight(List<WeightEntry> entries, int daysAhead) {
        if (entries == null || entries.size() < FormulaConstants.MIN_DATA_POINTS_FOR_PREDICTION) {
            return 0.0; // Not enough data for prediction
        }

        double currentWeight = entries.get(entries.size() - 1).getWeight();
        double dailyRate = calculateWeightChangeRate(entries);
        double predictedWeight = currentWeight + (dailyRate * daysAhead);

        return Math.round(predictedWeight * 10.0) / 10.0;
    }

    /**
     * Calculates days to reach goal weight based on current trend.
     * Returns -1 if goal is not achievable with current trend.
     *
     * Time Complexity: O(n)
     *
     * @param entries List of weight entries
     * @param goalWeight Target weight in kg
     * @return Days to reach goal, or -1 if not achievable
     */
    public static int daysToGoal(List<WeightEntry> entries, double goalWeight) {
        if (entries == null || entries.isEmpty()) {
            return -1;
        }

        double currentWeight = entries.get(entries.size() - 1).getWeight();
        double dailyRate = calculateWeightChangeRate(entries);

        // Check if trend is moving toward goal
        double difference = goalWeight - currentWeight;

        if (Math.abs(dailyRate) < 0.001) {
            return -1; // No progress (maintaining)
        }

        if ((difference > 0 && dailyRate < 0) || (difference < 0 && dailyRate > 0)) {
            return -1; // Moving away from goal
        }

        int days = (int) Math.abs(difference / dailyRate);
        return days;
    }

    /**
     * Calculates standard deviation of weights.
     * Useful for detecting consistency.
     *
     * Time Complexity: O(n)
     *
     * @param entries List of weight entries
     * @return Standard deviation in kg
     */
    public static double calculateStandardDeviation(List<WeightEntry> entries) {
        if (entries == null || entries.size() < 2) {
            return 0.0;
        }

        // Calculate mean
        double sum = 0.0;
        for (WeightEntry entry : entries) {
            sum += entry.getWeight();
        }
        double mean = sum / entries.size();

        // Calculate variance
        double varianceSum = 0.0;
        for (WeightEntry entry : entries) {
            double diff = entry.getWeight() - mean;
            varianceSum += diff * diff;
        }
        double variance = varianceSum / entries.size();

        // Standard deviation is square root of variance
        return Math.round(Math.sqrt(variance) * 100.0) / 100.0;
    }
}