package com.example.optiononeweighttrackingappjacobsegarra.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index; // New Import
import androidx.room.PrimaryKey;

/**
 * Room Entity representing the 'user_table'.
 * Stores user credentials (username/password) and settings (goal weight/phone number).
 */
@Entity(tableName = "user_table",
        indices = {@Index(value = {"username"}, unique = true)}) // Ensures username is unique
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "goalWeight")
    private double goalWeight;

    // Phone number for SMS alerts
    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;

    // --- Constructor used by RegistrationActivity ---
    public User(String username, String password, double goalWeight, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.goalWeight = goalWeight;
        this.phoneNumber = phoneNumber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}