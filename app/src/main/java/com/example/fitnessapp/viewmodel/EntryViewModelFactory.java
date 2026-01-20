package com.example.fitnessapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Custom Factory class required to instantiate the EntryViewModel
 * because its constructor takes a non-standard argument (the userId).
 */
public class EntryViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final int userId;

    /**
     * @param application The application context.
     * @param userId The ID of the currently logged-in user.
     */
    public EntryViewModelFactory(Application application, int userId) {
        this.application = application;
        this.userId = userId;
    }

    /**
     * Creates a new instance of the EntryViewModel.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EntryViewModel.class)) {
            try {
                return (T) new EntryViewModel(application, userId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot instantiate EntryViewModel: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}