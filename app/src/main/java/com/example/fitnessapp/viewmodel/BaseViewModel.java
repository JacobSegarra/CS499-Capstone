package com.example.fitnessapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Base ViewModel class that provides common functionality for all ViewModels.
 * Includes error handling, loading states, and shared utilities.
 */
public abstract class BaseViewModel extends AndroidViewModel {

    // Loading state - indicates when async operations are in progress
    protected final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    // Error state - holds error messages to display to the user
    protected final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    // Success message state - holds success messages to display to the user
    protected final MutableLiveData<String> _successMessage = new MutableLiveData<>();
    public final LiveData<String> successMessage = _successMessage;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Sets the loading state.
     *
     * @param isLoading true if loading, false otherwise
     */
    protected void setLoading(boolean isLoading) {
        _isLoading.postValue(isLoading);
    }

    /**
     * Sets an error message to be displayed to the user.
     *
     * @param message The error message
     */
    protected void setError(String message) {
        _errorMessage.postValue(message);
        setLoading(false);
    }

    /**
     * Sets a success message to be displayed to the user.
     *
     * @param message The success message
     */
    protected void setSuccess(String message) {
        _successMessage.postValue(message);
        setLoading(false);
    }

    /**
     * Clears any error message.
     */
    public void clearError() {
        _errorMessage.postValue(null);
    }

    /**
     * Clears any success message.
     */
    public void clearSuccess() {
        _successMessage.postValue(null);
    }

    /**
     * Clears all messages (error and success).
     */
    public void clearMessages() {
        clearError();
        clearSuccess();
    }
}