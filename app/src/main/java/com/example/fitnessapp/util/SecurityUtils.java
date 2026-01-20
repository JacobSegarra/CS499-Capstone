package com.example.fitnessapp.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Security utility class for password hashing and verification.
 * Uses BCrypt algorithm for secure password storage.
 */
public class SecurityUtils {

    // BCrypt cost factor (higher = more secure but slower)
    // 12 is a good balance between security and performance
    private static final int BCRYPT_COST = 12;

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainPassword The plain-text password to hash
     * @return The hashed password string
     * @throws IllegalArgumentException if password is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        return BCrypt.withDefaults().hashToString(BCRYPT_COST, plainPassword.toCharArray());
    }

    /**
     * Verifies a plain-text password against a hashed password.
     *
     * @param plainPassword The plain-text password to verify
     * @param hashedPassword The hashed password to verify against
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}