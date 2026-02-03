package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity for saving workout templates (routines).
 * Stores exercise IDs as JSON for easy retrieval.
 */
@Entity(
        tableName = "workout_templates",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "userId")
        }
)
public class WorkoutTemplate {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String templateName;
    private String description;
    private String exercises;         // JSON array of exercise IDs: "[1,5,8,12]"
    private long createdAt;
    private Long lastUsed;            // NULL if never used, timestamp of last use

    // Constructor
    public WorkoutTemplate(int userId, String templateName) {
        this.userId = userId;
        this.templateName = templateName;
        this.exercises = "[]";
        this.createdAt = System.currentTimeMillis();
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Long lastUsed) {
        this.lastUsed = lastUsed;
    }

    /**
     * Mark template as used (updates lastUsed timestamp).
     */
    public void markAsUsed() {
        this.lastUsed = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "WorkoutTemplate{name='" + templateName + "', exercises=" + exercises + "}";
    }
}