package com.example.fitnessapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessapp.data.User;
import com.example.fitnessapp.repository.EntryRepository;
import com.example.fitnessapp.util.ValidationUtils;

/**
 * ViewModel for SettingsActivity.
 * Handles user settings updates with validation.
 */
public class SettingsViewModel extends BaseViewModel {

    private final EntryRepository repository;
    private final int userId;

    // Current user data
    private final LiveData<User> currentUser;

    // Update success indicator
    private final MutableLiveData<Boolean> _updateSuccess = new MutableLiveData<>();
    public final LiveData<Boolean> updateSuccess = _updateSuccess;

    public SettingsViewModel(@NonNull Application application, int userId) {
        super(application);
        this.repository = new EntryRepository(application);
        this.userId = userId;
        this.currentUser = repository.getCurrentUser(userId);
    }

    /**
     * Gets the current user data as LiveData.
     *
     * @return LiveData containing the User object
     */
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    /**
     * Updates the user's goal weight with validation.
     *
     * @param newGoalWeight The new goal weight in kg
     */
    public void updateGoalWeight(double newGoalWeight) {
        clearMessages();

        // Validate goal weight
        ValidationUtils.ValidationResult validation = ValidationUtils.validateGoalWeight(newGoalWeight);
        if (validation.isInvalid()) {
            setError(validation.getErrorMessage());
            return;
        }

        setLoading(true);

        // Get current user and update
        User user = currentUser.getValue();
        if (user == null) {
            setError("User data not loaded");
            return;
        }

        user.setGoalWeight(newGoalWeight);
        repository.updateUser(user);

        _updateSuccess.postValue(true);
        setSuccess("Goal weight updated successfully!");
    }

    /**
     * Updates the user's phone number with validation.
     *
     * @param newPhoneNumber The new phone number
     */
    public void updatePhoneNumber(String newPhoneNumber) {
        clearMessages();

        // Validate phone number
        ValidationUtils.ValidationResult validation = ValidationUtils.validatePhone(newPhoneNumber);
        if (validation.isInvalid()) {
            setError(validation.getErrorMessage());
            return;
        }

        setLoading(true);

        // Get current user and update
        User user = currentUser.getValue();
        if (user == null) {
            setError("User data not loaded");
            return;
        }

        user.setPhoneNumber(newPhoneNumber);
        repository.updateUser(user);

        _updateSuccess.postValue(true);
        setSuccess("Phone number updated successfully!");
    }

    /**
     * Clears the update success flag.
     */
    public void clearUpdateSuccess() {
        _updateSuccess.setValue(false);
    }
}