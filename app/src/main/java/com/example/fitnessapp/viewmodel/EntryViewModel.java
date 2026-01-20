package com.example.fitnessapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnessapp.data.User;
import com.example.fitnessapp.data.WeightEntry;
import com.example.fitnessapp.repository.EntryRepository;

import java.util.List;

/**
 * The ViewModel for the weight entries. It provides LiveData to the UI
 * and handles data operations by calling methods in the EntryRepository.
 */
public class EntryViewModel extends AndroidViewModel {

    private final EntryRepository repository;
    private final LiveData<List<WeightEntry>> allEntriesForUser;
    private final LiveData<User> currentUser;
    private final int userId;

    /**
     * Constructor for the ViewModel. It receives the Application context and the logged-in userId.
     * @param application The application context.
     * @param userId The ID of the currently logged-in user.
     */
    public EntryViewModel(@NonNull Application application, int userId) {
        super(application);
        this.userId = userId;
        this.repository = new EntryRepository(application);

        // Fetch LiveData for entries and user
        this.allEntriesForUser = repository.getAllEntriesForUser(userId);
        this.currentUser = repository.getCurrentUser(userId);
    }

    /**
     * Returns the LiveData list of entries for the user to be observed by the UI.
     * @return LiveData<List<WeightEntry>>
     */
    public LiveData<List<WeightEntry>> getAllEntriesForUser() {
        return allEntriesForUser;
    }

    /**
     * ADDED: Returns the current user as LiveData.
     * @return LiveData<User>
     */
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    /**
     * Inserts a new weight entry via the repository.
     * @param entry The WeightEntry object to insert.
     */
    public void insertWeightEntry(WeightEntry entry) {
        repository.insertWeightEntry(entry);
    }

    /**
     * Updates an existing weight entry via the repository.
     * @param entry The WeightEntry object to update.
     */
    public void updateWeightEntry(WeightEntry entry) {
        repository.updateWeightEntry(entry);
    }

    /**
     * Deletes a weight entry via the repository.
     * @param entry The WeightEntry object to delete.
     */
    public void deleteWeightEntry(WeightEntry entry) {
        repository.deleteWeightEntry(entry);
    }

    /**
     * Updates user settings (goal weight, phone number).
     * @param user The updated User object.
     */
    public void updateUser(User user) {
        repository.updateUser(user);
    }
}