package com.example.fitnessapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Room Entity for the 'user_table'.
 * Stores user credentials (username/passwordHash) and settings (goal weight/phone number).
 * Passwords are hashed using BCrypt before storage for security.
 */
@Entity(tableName = "user_table",
        indices = {@Index(value = {"username"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "passwordHash")
    private String passwordHash;

    @ColumnInfo(name = "goalWeight")
    private double goalWeight;

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;

    @ColumnInfo(name = "preferredUnit")
    private String preferredUnit;  // "lbs" or "kg"

    // Constructor used by RegistrationActivity and Repository
    public User(String username, String passwordHash, double goalWeight, String phoneNumber) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.goalWeight = goalWeight;
        this.phoneNumber = phoneNumber;
        this.preferredUnit = "lbs";  // Default to pounds
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPreferredUnit() {
        return preferredUnit;
    }

    public void setPreferredUnit(String preferredUnit) {
        this.preferredUnit = preferredUnit;
    }
}