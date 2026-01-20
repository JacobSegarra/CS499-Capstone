package com.example.fitnessapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessapp.repository.EntryRepository;
import com.example.fitnessapp.util.Result;
import com.example.fitnessapp.data.AppDatabase;

/**
 * ViewModel for RegistrationActivity.
 * Handles user registration logic with validation and password hashing.
 */
public class RegistrationViewModel extends BaseViewModel {

    private final EntryRepository repository;

    // Registration result - contains the new user ID on success
    private final MutableLiveData<Long> _registrationResult = new MutableLiveData<>();
    public final LiveData<Long> registrationResult = _registrationResult;

    // Username availability check (for real-time validation)
    private final MutableLiveData<Boolean> _isUsernameAvailable = new MutableLiveData<>();
    public final LiveData<Boolean> isUsernameAvailable = _isUsernameAvailable;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        repository = new EntryRepository(application);
    }

    /**
     * Registers a new user with the provided information.
     * Performs comprehensive validation and password hashing.
     *
     * @param username The desired username
     * @param password The desired password
     * @param confirmPassword The password confirmation
     * @param goalWeight The user's goal weight in kg
     * @param phoneNumber The user's phone number for SMS
     */
    public void registerUser(String username, String password, String confirmPassword,
                             double goalWeight, String phoneNumber) {
        // Clear previous messages
        clearMessages();

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            setError("Passwords do not match");
            return;
        }

        // Show loading state
        setLoading(true);

        // Perform registration on background thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Result<Long> result = repository.registerUser(username, password, goalWeight, phoneNumber);

            if (result.isSuccess()) {
                _registrationResult.postValue(result.getData());
                setSuccess("Registration successful!");
            } else {
                setError(result.getErrorMessage());
            }
        });
    }

    /**
     * Checks if a username is available
     *
     * @param username The username to check
     */
    public void checkUsernameAvailability(String username) {
        if (username == null || username.trim().isEmpty()) {
            _isUsernameAvailable.postValue(false);
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            boolean available = repository.isUsernameAvailable(username.trim());
            _isUsernameAvailable.postValue(available);
        });
    }

    /**
     * Clears the registration result
     */
    public void clearRegistrationResult() {
        _registrationResult.setValue(null);
    }
}