package com.example.fitnessapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessapp.data.User;
import com.example.fitnessapp.repository.EntryRepository;
import com.example.fitnessapp.util.Result;
import com.example.fitnessapp.data.AppDatabase;

/**
 * ViewModel for LoginActivity.
 * Handles user authentication logic and exposes UI state.
 */
public class LoginViewModel extends BaseViewModel {

    private final EntryRepository repository;

    // Authentication result - triggers navigation on success
    private final MutableLiveData<User> _authenticatedUser = new MutableLiveData<>();
    public final LiveData<User> authenticatedUser = _authenticatedUser;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new EntryRepository(application);
    }

    /**
     * Attempts to authenticate a user with the provided credentials.
     * Performs validation and BCrypt password verification.
     *
     * @param username The username entered by the user
     * @param password The password entered by the user
     */
    public void login(String username, String password) {
        // Clear previous messages
        clearMessages();

        // Basic input validation
        if (username == null || username.trim().isEmpty()) {
            setError("Please enter a username");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            setError("Please enter a password");
            return;
        }

        // Show loading state
        setLoading(true);

        // Perform authentication on background thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Result<User> result = repository.authenticateUser(username.trim(), password);

            if (result.isSuccess()) {
                _authenticatedUser.postValue(result.getData());
                setSuccess("Login successful!");
            } else {
                setError(result.getErrorMessage());
            }
        });
    }

    /**
     * Clears the authenticated user
     */
    public void clearAuthenticatedUser() {
        _authenticatedUser.setValue(null);
    }
}