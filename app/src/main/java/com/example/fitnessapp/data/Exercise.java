package com.example.fitnessapp.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing an exercise in the database.
 * Can be database exercise or user-created custom exercise.
 */
@Entity(
        tableName = "exercises",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = "name"),
                @Index(value = "category"),
                @Index(value = "userId")
        }
)
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String category;           // 'chest', 'back', 'legs', 'shoulders', 'arms', 'core', 'cardio'
    private String equipmentType;      // 'barbell', 'dumbbell', 'machine', 'bodyweight', 'cable'
    private String primaryMuscle;
    private String secondaryMuscle;
    private String description;
    private boolean isCustom;          // false = database exercise, true = user-created
    private Integer userId;            // NULL for database exercises, user ID for custom
    private long createdAt;

    // Constructor
    public Exercise(String name, String category) {
        this.name = name;
        this.category = category;
        this.isCustom = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    public String getSecondaryMuscle() {
        return secondaryMuscle;
    }

    public void setSecondaryMuscle(String secondaryMuscle) {
        this.secondaryMuscle = secondaryMuscle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Exercise{name='" + name + "', category='" + category + "', equipment='" + equipmentType + "'}";
    }
}