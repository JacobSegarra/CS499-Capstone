package com.example.fitnessapp.algorithm;

import com.example.fitnessapp.data.WeightEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Generates sample weight data for testing statistical algorithms.
 * Creates realistic weight progression data with natural fluctuations.
 */
public class SampleDataGenerator {

    private static final Random random = new Random();

    /**
     * Generates a list of weight entries with a losing weight trend.
     * Simulates realistic daily weight fluctuations around a downward trend.
     *
     * @param userId The user ID for the entries
     * @param startWeight Starting weight in kg
     * @param targetWeight Target weight in kg
     * @param days Number of days of data to generate
     * @return List of WeightEntry objects with realistic weight progression
     */
    public static List<WeightEntry> generateWeightLossTrend(int userId, double startWeight,
                                                            double targetWeight, int days) {
        List<WeightEntry> entries = new ArrayList<>();

        // Calculate daily weight change (negative for weight loss)
        double totalChange = targetWeight - startWeight;
        double avgDailyChange = totalChange / days;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days); // Start 'days' ago

        double currentWeight = startWeight;

        for (int i = 0; i < days; i++) {
            // Add random daily fluctuation (±0.3 kg)
            double fluctuation = (random.nextDouble() - 0.5) * 0.6;
            currentWeight += avgDailyChange + fluctuation;

            // Ensure weight doesn't go below target or above start
            currentWeight = Math.max(targetWeight, Math.min(startWeight, currentWeight));

            // Round to 1 decimal place
            currentWeight = Math.round(currentWeight * 10.0) / 10.0;

            WeightEntry entry = new WeightEntry(userId, currentWeight, calendar.getTimeInMillis());
            entries.add(entry);

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return entries;
    }

    /**
     * Generates a list of weight entries with a gaining weight trend.
     *
     * @param userId The user ID for the entries
     * @param startWeight Starting weight in kg
     * @param targetWeight Target weight in kg (higher than start)
     * @param days Number of days of data to generate
     * @return List of WeightEntry objects with weight gain progression
     */
    public static List<WeightEntry> generateWeightGainTrend(int userId, double startWeight,
                                                            double targetWeight, int days) {
        // Same logic as weight loss but with positive change
        return generateWeightLossTrend(userId, startWeight, targetWeight, days);
    }

    /**
     * Generates weight entries with no clear trend (maintenance).
     * Weight fluctuates randomly around a center point.
     *
     * @param userId The user ID for the entries
     * @param weight The maintenance weight in kg
     * @param days Number of days of data to generate
     * @return List of WeightEntry objects with stable weight
     */
    public static List<WeightEntry> generateMaintenanceTrend(int userId, double weight, int days) {
        List<WeightEntry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);

        for (int i = 0; i < days; i++) {
            // Random fluctuation around maintenance weight (±0.5 kg)
            double fluctuation = (random.nextDouble() - 0.5) * 1.0;
            double currentWeight = weight + fluctuation;

            // Round to 1 decimal place
            currentWeight = Math.round(currentWeight * 10.0) / 10.0;

            WeightEntry entry = new WeightEntry(userId, currentWeight, calendar.getTimeInMillis());
            entries.add(entry);

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return entries;
    }

    /**
     * Generates realistic 30-day weight loss data for demo purposes.
     * Typical scenario: User losing ~0.5 kg per week (healthy rate).
     *
     * @param userId The user ID for the entries
     * @return List of 30 WeightEntry objects showing healthy weight loss
     */
    public static List<WeightEntry> generateRealistic30DayData(int userId) {
        // Starting at 85kg, losing to 83kg over 30 days (~2kg/month = healthy rate)
        return generateWeightLossTrend(userId, 85.0, 83.0, 30);
    }
}