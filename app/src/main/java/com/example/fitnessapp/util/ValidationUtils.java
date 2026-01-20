package com.example.fitnessapp.util;

import java.util.regex.Pattern;

/**
 * Validation utility class for input validation across the application.
 * Provides regex-based validation for usernames, passwords, phone numbers, etc.
 */
public class ValidationUtils {

    // Username: 4-20 alphanumeric characters (letters, numbers, underscores)
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");

    // Password: Minimum 8 characters, at least one uppercase, one lowercase, one number
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    // Phone: 10 digits (US format)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    /**
     * Validates a username.
     *
     * @param username The username to validate
     * @return ValidationResult containing success status and error message if applicable
     */
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return ValidationResult.failure("Username cannot be empty");
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return ValidationResult.failure(
                    "Username must be 4-20 characters (letters, numbers, underscores only)"
            );
        }

        return ValidationResult.success();
    }

    /**
     * Validates a password.
     *
     * @param password The password to validate
     * @return ValidationResult containing success status and error message if applicable
     */
    public static ValidationResult validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return ValidationResult.failure("Password cannot be empty");
        }

        if (password.length() < 8) {
            return ValidationResult.failure("Password must be at least 8 characters");
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return ValidationResult.failure(
                    "Password must contain at least one uppercase letter, one lowercase letter, and one number"
            );
        }

        return ValidationResult.success();
    }

    /**
     * Validates a phone number (US format: 10 digits).
     *
     * @param phone The phone number to validate
     * @return ValidationResult containing success status and error message if applicable
     */
    public static ValidationResult validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return ValidationResult.failure("Phone number cannot be empty");
        }

        // Remove common formatting characters
        String cleanedPhone = phone.replaceAll("[\\s()-]", "");

        if (!PHONE_PATTERN.matcher(cleanedPhone).matches()) {
            return ValidationResult.failure("Phone number must be 10 digits");
        }

        return ValidationResult.success();
    }

    /**
     * Validates a weight value.
     *
     * @param weight The weight value to validate
     * @return ValidationResult containing success status and error message if applicable
     */
    public static ValidationResult validateWeight(double weight) {
        if (weight <= 0) {
            return ValidationResult.failure("Weight must be greater than 0");
        }

        if (weight > 500) {
            return ValidationResult.failure("Weight must be less than 500 kg");
        }

        return ValidationResult.success();
    }

    /**
     * Validates a goal weight value.
     *
     * @param goalWeight The goal weight to validate
     * @return ValidationResult containing success status and error message if applicable
     */
    public static ValidationResult validateGoalWeight(double goalWeight) {
        return validateWeight(goalWeight); // Same validation as regular weight
    }

    /**
     * Inner class representing the result of a validation operation.
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        private ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult failure(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() {
            return isValid;
        }

        public boolean isInvalid() {
            return !isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}