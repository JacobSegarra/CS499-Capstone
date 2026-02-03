package com.example.fitnessapp.util;

import java.util.Locale;

/**
 * Utility class for converting between imperial (lbs) and metric (kg) units.
 * All weights in the database are stored in the user's preferred unit.
 */
public class UnitConverter {

    // Conversion constant
    private static final double LBS_TO_KG = 0.45359237;
    private static final double KG_TO_LBS = 2.20462262;

    /**
     * Convert pounds to kilograms.
     */
    public static double lbsToKg(double lbs) {
        return lbs * LBS_TO_KG;
    }

    /**
     * Convert kilograms to pounds.
     */
    public static double kgToLbs(double kg) {
        return kg * KG_TO_LBS;
    }

    /**
     * Convert weight to user's preferred unit.
     * If weight is stored in kg and user wants lbs, convert it.
     * If weight is stored in lbs and user wants kg, convert it.
     *
     * @param weight The weight value
     * @param storedUnit The unit the weight is currently in ("lbs" or "kg")
     * @param preferredUnit The unit the user wants ("lbs" or "kg")
     * @return Converted weight
     */
    public static double convertWeight(double weight, String storedUnit, String preferredUnit) {
        if (storedUnit.equals(preferredUnit)) {
            return weight;  // No conversion needed
        }

        if (storedUnit.equals("kg") && preferredUnit.equals("lbs")) {
            return kgToLbs(weight);
        } else if (storedUnit.equals("lbs") && preferredUnit.equals("kg")) {
            return lbsToKg(weight);
        }

        return weight;  // Default, no conversion
    }

    /**
     * Format weight with appropriate unit label.
     *
     * @param weight The weight value
     * @param unit The unit ("lbs" or "kg")
     * @param decimals Number of decimal places
     * @return Formatted string like "185.5 lbs" or "84.2 kg"
     */
    public static String formatWeight(double weight, String unit, int decimals) {
        String format = "%." + decimals + "f %s";
        return String.format(Locale.getDefault(), format, weight, unit);
    }

    /**
     * Format weight with 1 decimal place (default).
     */
    public static String formatWeight(double weight, String unit) {
        return formatWeight(weight, unit, 1);
    }

    /**
     * Get the unit label for display.
     */
    public static String getUnitLabel(String unit) {
        if ("kg".equalsIgnoreCase(unit)) {
            return "kg";
        } else {
            return "lbs";
        }
    }

    /**
     * Validate weight is in reasonable range for the given unit.
     *
     * @param weight The weight value
     * @param unit The unit ("lbs" or "kg")
     * @return true if valid, false if out of range
     */
    public static boolean isValidWeight(double weight, String unit) {
        if (weight <= 0) {
            return false;
        }

        if ("kg".equalsIgnoreCase(unit)) {
            // Valid range: 20-300 kg (44-661 lbs)
            return weight >= 20 && weight <= 300;
        } else {
            // Valid range: 44-661 lbs (20-300 kg)
            return weight >= 44 && weight <= 661;
        }
    }

    /**
     * Get min/max weight for validation based on unit.
     */
    public static double getMinWeight(String unit) {
        return "kg".equalsIgnoreCase(unit) ? 20.0 : 44.0;
    }

    public static double getMaxWeight(String unit) {
        return "kg".equalsIgnoreCase(unit) ? 300.0 : 661.0;
    }

    /**
     * Parse weight input and validate.
     * Returns null if invalid.
     */
    public static Double parseWeight(String input, String unit) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        try {
            double weight = Double.parseDouble(input.trim());
            if (isValidWeight(weight, unit)) {
                return weight;
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return null;
    }
}