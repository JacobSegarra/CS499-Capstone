package com.example.fitnessapp.model;

/**
 * Model class representing workout performance metrics.
 * Holds 1RM estimates, volume calculations, and strength assessments.
 */
public class WorkoutMetrics {

    private final double weight;
    private final int reps;
    private final double estimatedOneRM;
    private final double volume;
    private final double percentOf1RM;
    private final String strengthLevel;
    private final boolean isProgressiveOverload;
    private final double volumeImprovement;

    public WorkoutMetrics(double weight,
                          int reps,
                          double estimatedOneRM,
                          double volume,
                          double percentOf1RM,
                          String strengthLevel,
                          boolean isProgressiveOverload,
                          double volumeImprovement) {
        this.weight = weight;
        this.reps = reps;
        this.estimatedOneRM = estimatedOneRM;
        this.volume = volume;
        this.percentOf1RM = percentOf1RM;
        this.strengthLevel = strengthLevel;
        this.isProgressiveOverload = isProgressiveOverload;
        this.volumeImprovement = volumeImprovement;
    }

    // Getters
    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public double getEstimatedOneRM() {
        return estimatedOneRM;
    }

    public double getVolume() {
        return volume;
    }

    public double getPercentOf1RM() {
        return percentOf1RM;
    }

    public String getStrengthLevel() {
        return strengthLevel;
    }

    public boolean isProgressiveOverload() {
        return isProgressiveOverload;
    }

    public double getVolumeImprovement() {
        return volumeImprovement;
    }

    /**
     * Gets human-readable performance summary.
     */
    public String getPerformanceSummary() {
        return String.format(
                "Set: %.1fkg × %d reps | Est 1RM: %.1fkg\n" +
                        "Volume: %.1fkg | Intensity: %.1f%% of 1RM\n" +
                        "Strength Level: %s",
                weight, reps, estimatedOneRM,
                volume, percentOf1RM,
                strengthLevel
        );
    }

    /**
     * Gets progress indicator message.
     */
    public String getProgressMessage() {
        if (isProgressiveOverload) {
            return String.format("✓ Progressive overload achieved! +%.1f%% volume", volumeImprovement);
        } else if (volumeImprovement > 0) {
            return String.format("Slight improvement: +%.1f%% volume", volumeImprovement);
        } else if (volumeImprovement < 0) {
            return String.format("Volume decreased: %.1f%%", volumeImprovement);
        } else {
            return "First workout - establish baseline";
        }
    }

    @Override
    public String toString() {
        return "WorkoutMetrics{" +
                "weight=" + weight +
                "kg × " + reps +
                ", 1RM=" + estimatedOneRM + "kg" +
                ", volume=" + volume + "kg" +
                ", level='" + strengthLevel + '\'' +
                '}';
    }
}