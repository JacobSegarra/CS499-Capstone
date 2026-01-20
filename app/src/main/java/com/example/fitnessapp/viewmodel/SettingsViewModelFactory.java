package com.example.fitnessapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory for creating SettingsViewModel instances.
 */
public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final int userId;

    public SettingsViewModelFactory(Application application, int userId) {
        this.application = application;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(application, userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}