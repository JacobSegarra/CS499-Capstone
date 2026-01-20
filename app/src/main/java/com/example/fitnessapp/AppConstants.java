package com.example.fitnessapp;

/**
 * Global application constants.
 * The core assumption is that the database stores weight in Kilograms (KG),
 * but the user interacts with the app using Pounds (LBS).
 */
public class AppConstants {
    // Conversion factor: 1 pound (lb) = 0.453592 kilograms (kg)
    public static final double KG_PER_LBS = 0.453592;
    // Conversion factor: 1 kilogram (kg) = 2.20462 pounds (lbs)
    public static final double LBS_PER_KG = 1.0 / KG_PER_LBS;
}
