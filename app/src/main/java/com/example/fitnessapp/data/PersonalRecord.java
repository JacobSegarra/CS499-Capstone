package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity for tracking personal records.
 * Automatically updated when user achieves new PR.
 */
@Entity(
        tableName = "personal_records",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = WorkoutSet.class,
                        parentColumns = "id",
                        childColumns = "workoutSetId",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = "userId"),
                @Index(value = "exerciseId"),
                @Index(value = {"userId", "exerciseId", "recordType"}, unique = true)
        }
)
public class PersonalRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int exerciseId;
    private String recordType;        // 'max_weight', 'max_volume', 'max_reps', 'estimated_1rm'
    private double recordValue;       // The actual record value (lbs or count)
    private Integer reps;             // For max_weight records (how many reps at that weight)
    private String dateAchieved;      // 'YYYY-MM-DD'
    private Integer workoutSetId;     // Reference to the set that achieved this PR

    // Constructor
    public PersonalRecord(int userId, int exerciseId, String recordType, double recordValue, String dateAchieved) {
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.recordType = recordType;
        this.recordValue = recordValue;
        this.dateAchieved = dateAchieved;
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

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public double getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(double recordValue) {
        this.recordValue = recordValue;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public String getDateAchieved() {
        return dateAchieved;
    }

    public void setDateAchieved(String dateAchieved) {
        this.dateAchieved = dateAchieved;
    }

    public Integer getWorkoutSetId() {
        return workoutSetId;
    }

    public void setWorkoutSetId(Integer workoutSetId) {
        this.workoutSetId = workoutSetId;
    }

    /**
     * Get formatted record description.
     */
    public String getRecordDescription() {
        switch (recordType) {
            case "max_weight":
                return String.format("%.0f lbs x %d reps", recordValue, reps != null ? reps : 1);
            case "max_reps":
                return String.format("%d reps", (int)recordValue);
            case "max_volume":
                return String.format("%.0f lbs total", recordValue);
            case "estimated_1rm":
                return String.format("%.0f lbs (est. 1RM)", recordValue);
            default:
                return String.format("%.0f", recordValue);
        }
    }

    @Override
    public String toString() {
        return "PersonalRecord{exercise=" + exerciseId + ", type='" + recordType +
                "', value=" + String.format("%.0f", recordValue) + "}";
    }
}