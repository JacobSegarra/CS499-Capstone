package com.example.fitnessapp.model;

/**
 * Model class representing the results of weight trend statistical analysis.
 * Holds all calculated metrics from StatisticalAnalyzer.
 */
public class WeightTrendAnalysis {

    private final double currentWeight;
    private final double sevenDayAverage;
    private final double thirtyDayAverage;
    private final double weeklyChangeRate;
    private final String trend; // "LOSING", "GAINING", "MAINTAINING"
    private final double predictedWeightIn30Days;
    private final int daysToGoal;
    private final double standardDeviation;

    public WeightTrendAnalysis(double currentWeight,
                               double sevenDayAverage,
                               double thirtyDayAverage,
                               double weeklyChangeRate,
                               String trend,
                               double predictedWeightIn30Days,
                               int daysToGoal,
                               double standardDeviation) {
        this.currentWeight = currentWeight;
        this.sevenDayAverage = sevenDayAverage;
        this.thirtyDayAverage = thirtyDayAverage;
        this.weeklyChangeRate = weeklyChangeRate;
        this.trend = trend;
        this.predictedWeightIn30Days = predictedWeightIn30Days;
        this.daysToGoal = daysToGoal;
        this.standardDeviation = standardDeviation;
    }

    // Getters
    public double getCurrentWeight() {
        return currentWeight;
    }

    public double getSevenDayAverage() {
        return sevenDayAverage;
    }

    public double getThirtyDayAverage() {
        return thirtyDayAverage;
    }

    public double getWeeklyChangeRate() {
        return weeklyChangeRate;
    }

    public String getTrend() {
        return trend;
    }

    public double getPredictedWeightIn30Days() {
        return predictedWeightIn30Days;
    }

    public int getDaysToGoal() {
        return daysToGoal;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * Gets a summary of the trend.
     */
    public String getTrendDescription() {
        switch (trend) {
            case "LOSING":
                return String.format("Losing %.1f kg/week", Math.abs(weeklyChangeRate));
            case "GAINING":
                return String.format("Gaining %.1f kg/week", weeklyChangeRate);
            case "MAINTAINING":
                return "Maintaining weight";
            default:
                return "Insufficient data";
        }
    }

    /**
     * Gets goal progress message.
     */
    public String getGoalProgressMessage() {
        if (daysToGoal < 0) {
            return "Current trend is not moving toward goal";
        } else if (daysToGoal == 0) {
            return "Goal weight reached!";
        } else {
            int weeks = daysToGoal / 7;
            return String.format("Estimated %d days (%d weeks) to goal", daysToGoal, weeks);
        }
    }

    @Override
    public String toString() {
        return "WeightTrendAnalysis{" +
                "currentWeight=" + currentWeight +
                ", trend='" + trend + '\'' +
                ", weeklyChange=" + weeklyChangeRate +
                ", predictedIn30Days=" + predictedWeightIn30Days +
                ", daysToGoal=" + daysToGoal +
                '}';
    }
}