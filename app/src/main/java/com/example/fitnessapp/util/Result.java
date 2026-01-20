package com.example.fitnessapp.util;

/**
 * A  Result wrapper class for handling success and failure states.
 * This replaces throwing exceptions and provides structured error handling.
 *
 * @param <T> The type of data returned on success
 */
public class Result<T> {
    private final T data;
    private final String errorMessage;
    private final boolean isSuccess;

    private Result(T data, String errorMessage, boolean isSuccess) {
        this.data = data;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    /**
     * Creates a successful result with data.
     *
     * @param data The successful data
     * @param <T> The type of data
     * @return A successful Result containing the data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true);
    }

    /**
     * Creates a failure result with an error message.
     *
     * @param errorMessage The error message describing the failure
     * @param <T> The expected type (will be null on failure)
     * @return A failed Result containing the error message
     */
    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    /**
     * Checks if the result represents a success.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Checks if the result represents a failure.
     *
     * @return true if failed, false otherwise
     */
    public boolean isFailure() {
        return !isSuccess;
    }

    /**
     * Gets the data if the result is successful.
     * Returns null if the result is a failure.
     *
     * @return The data on success, null on failure
     */
    public T getData() {
        return data;
    }

    /**
     * Gets the error message if the result is a failure.
     * Returns null if the result is successful.
     *
     * @return The error message on failure, null on success
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the error message or a default message if null.
     *
     * @param defaultMessage The default message to return if errorMessage is null
     * @return The error message or default
     */
    public String getErrorMessageOrDefault(String defaultMessage) {
        return errorMessage != null ? errorMessage : defaultMessage;
    }
}