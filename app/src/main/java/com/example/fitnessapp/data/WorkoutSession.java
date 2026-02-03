package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a workout session (one workout on a specific date).
 * Total volume is auto-calculated from associated workout sets.
 */
@Entity(
        tableName = "workout_sessions",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "userId"),
                @Index(value = "date"),
                @Index(value = {"userId", "date"})
        }
)
public class WorkoutSession {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String date;              // 'YYYY-MM-DD'
    private long timestamp;
    private int durationMinutes;
    private String notes;
    private double totalVolume;       // Auto-calculated: sum of (weight x reps) for all sets
    private int totalSets;            // Auto-calculated: count of sets

    // Constructor
    public WorkoutSession(int userId, String date) {
        this.userId = userId;
        this.date = date;
        this.timestamp = System.currentTimeMillis();
        this.durationMinutes = 0;
        this.totalVolume = 0.0;
        this.totalSets = 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
    }

    /**
     * Add a set's volume to the session total.
     */
    public void addSetVolume(double weight, int reps) {
        this.totalVolume += (weight * reps);
        this.totalSets++;
    }

    @Override
    public String toString() {
        return "WorkoutSession{date='" + date + "', volume=" + String.format("%.0f", totalVolume) +
                ", sets=" + totalSets + "}";
    }
}