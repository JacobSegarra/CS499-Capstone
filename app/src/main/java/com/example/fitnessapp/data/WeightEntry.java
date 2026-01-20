package com.example.fitnessapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Room Entity representing a single weight entry.
 * It is linked to the User table via a ForeignKey constraint.
 */
@Entity(tableName = "weight_entry_table",
        foreignKeys = @ForeignKey(
                entity = User.class, // The parent entity
                parentColumns = "id", // The parent's primary key
                childColumns = "userId", // The column in this table that links to the parent
                onDelete = ForeignKey.CASCADE // If the parent user is deleted, delete all their entries
        ))
public class WeightEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Foreign Key linking to the User table
    @ColumnInfo(name = "userId")
    private int userId;

    @ColumnInfo(name = "weight")
    private double weight; // The weight value logged

    @ColumnInfo(name = "timestamp")
    private long timestamp; // Time of entry (System.currentTimeMillis())

    // --- Constructor used by MainActivity when logging a new entry ---
    public WeightEntry(int userId, double weight, long timestamp) {
        this.userId = userId;
        this.weight = weight;
        this.timestamp = timestamp;
    }

    // --- Getters and Setters (Required by Room) ---

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}