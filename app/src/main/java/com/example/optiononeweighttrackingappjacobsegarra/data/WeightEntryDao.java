package com.example.optiononeweighttrackingappjacobsegarra.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for the WeightEntry entity.
 * Provides methods for interacting with the weight_entry_table in the database.
 */
@Dao
public interface WeightEntryDao {

    /**
     * Inserts a new weight entry into the database.
     * @param entry The WeightEntry object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(WeightEntry entry);

    /**
     * Updates an existing weight entry.
     * @param entry The WeightEntry object to update.
     */
    @Update
    void update(WeightEntry entry);

    /**
     * Deletes a specific weight entry from the database.
     * @param entry The WeightEntry object to delete.
     */
    @Delete
    void delete(WeightEntry entry);

    /**
     * Retrieves all weight entries for a specific user, ordered by the most recent first.
     * This is the method required by the EntryRepository.
     *
     * @param userId The ID of the user whose entries should be fetched.
     * @return A LiveData list of WeightEntry objects for the given user.
     */
    @Query("SELECT * FROM weight_entry_table WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<WeightEntry>> getAllEntriesForUser(int userId);
}