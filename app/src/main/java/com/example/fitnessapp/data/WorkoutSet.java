package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a single set within a workout session.
 * Estimated 1RM and volume are auto-calculated.
 */
@Entity(
        tableName = "workout_sets",
        foreignKeys = {
                @ForeignKey(
                        entity = WorkoutSession.class,
                        parentColumns = "id",
                        childColumns = "sessionId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index(value = "sessionId"),
                @Index(value = "exerciseId")
        }
)
public class WorkoutSet {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int sessionId;           // Which workout session
    private int exerciseId;          // Which exercise
    private int setNumber;           // 1, 2, 3, etc. within this exercise
    private double weightLbs;        // Weight in pounds
    private int reps;                // Number of repetitions
    private Integer rpe;             // Rate of Perceived Exertion (1-10), optional
    private String notes;
    private Double estimated1RM;     // Auto-calculated using WorkoutAnalyzer
    private Double volume;           // Auto-calculated: weight x reps
    private long timestamp;

    // Constructor
    public WorkoutSet(int sessionId, int exerciseId, int setNumber, double weightLbs, int reps) {
        this.sessionId = sessionId;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.weightLbs = weightLbs;
        this.reps = reps;
        this.volume = weightLbs * reps;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public double getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(double weightLbs) {
        this.weightLbs = weightLbs;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public Integer getRpe() {
        return rpe;
    }

    public void setRpe(Integer rpe) {
        this.rpe = rpe;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getEstimated1RM() {
        return estimated1RM;
    }

    public void setEstimated1RM(Double estimated1RM) {
        this.estimated1RM = estimated1RM;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Calculate volume (weight x reps).
     */
    public void calculateVolume() {
        this.volume = this.weightLbs * this.reps;
    }

    @Override
    public String toString() {
        return "WorkoutSet{exercise=" + exerciseId + ", set=" + setNumber +
                ", " + String.format("%.0f", weightLbs) + "lbs x " + reps + " reps}";
    }
}