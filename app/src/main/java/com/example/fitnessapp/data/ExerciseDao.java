package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for Exercise entity.
 */
@Dao
public interface ExerciseDao {

    // INSERT
    @Insert
    long insert(Exercise exercise);

    @Insert
    List<Long> insertAll(List<Exercise> exercises);

    // UPDATE
    @Update
    void update(Exercise exercise);

    // DELETE
    @Delete
    void delete(Exercise exercise);

    @Query("DELETE FROM exercises WHERE id = :exerciseId")
    void deleteById(int exerciseId);

    // QUERIES

    /**
     * Get all exercises (database + user custom).
     */
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    LiveData<List<Exercise>> getAllExercises();

    /**
     * Get exercise by ID.
     */
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    LiveData<Exercise> getExerciseById(int exerciseId);

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    Exercise getExerciseByIdSync(int exerciseId);

    /**
     * Get exercises by category.
     */
    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name ASC")
    LiveData<List<Exercise>> getExercisesByCategory(String category);

    /**
     * Search exercises by name.
     */
    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    LiveData<List<Exercise>> searchExercisesByName(String searchQuery);

    /**
     * Get database exercises only.
     */
    @Query("SELECT * FROM exercises WHERE isCustom = 0 ORDER BY name ASC")
    LiveData<List<Exercise>> getDatabaseExercises();

    /**
     * Get user's custom exercises.
     */
    @Query("SELECT * FROM exercises WHERE userId = :userId AND isCustom = 1 ORDER BY name ASC")
    LiveData<List<Exercise>> getUserCustomExercises(int userId);

    /**
     * Get exercises by equipment type.
     */
    @Query("SELECT * FROM exercises WHERE equipmentType = :equipment ORDER BY name ASC")
    LiveData<List<Exercise>> getExercisesByEquipment(String equipment);

    /**
     * Get all categories (distinct).
     */
    @Query("SELECT DISTINCT category FROM exercises WHERE category IS NOT NULL ORDER BY category ASC")
    LiveData<List<String>> getAllCategories();

    /**
     * Count exercises.
     */
    @Query("SELECT COUNT(*) FROM exercises")
    LiveData<Integer> getExerciseCount();
}