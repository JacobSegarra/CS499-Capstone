package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for WorkoutSet entity.
 */
@Dao
public interface WorkoutSetDao {

    // INSERT
    @Insert
    long insert(WorkoutSet set);

    @Insert
    List<Long> insertAll(List<WorkoutSet> sets);

    // UPDATE
    @Update
    void update(WorkoutSet set);

    // DELETE
    @Delete
    void delete(WorkoutSet set);

    @Query("DELETE FROM workout_sets WHERE id = :setId")
    void deleteById(int setId);

    @Query("DELETE FROM workout_sets WHERE sessionId = :sessionId")
    void deleteAllForSession(int sessionId);

    // QUERIES

    /**
     * Get all sets for a workout session.
     */
    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    LiveData<List<WorkoutSet>> getSetsForSession(int sessionId);

    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    List<WorkoutSet> getSetsForSessionSync(int sessionId);

    /**
     * Get set by ID.
     */
    @Query("SELECT * FROM workout_sets WHERE id = :setId")
    LiveData<WorkoutSet> getSetById(int setId);

    @Query("SELECT * FROM workout_sets WHERE id = :setId")
    WorkoutSet getSetByIdSync(int setId);

    /**
     * Get all sets for a specific exercise (across all sessions).
     */
    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    LiveData<List<WorkoutSet>> getSetsForExercise(int exerciseId);

    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    List<WorkoutSet> getSetsForExerciseSync(int exerciseId);

    /**
     * Get sets for a specific exercise in a session.
     */
    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId AND exerciseId = :exerciseId ORDER BY setNumber ASC")
    LiveData<List<WorkoutSet>> getSetsForExerciseInSession(int sessionId, int exerciseId);

    /**
     * Get max weight for an exercise.
     */
    @Query("SELECT MAX(weightLbs) FROM workout_sets WHERE exerciseId = :exerciseId")
    LiveData<Double> getMaxWeightForExercise(int exerciseId);

    /**
     * Get max reps for an exercise at a specific weight.
     */
    @Query("SELECT MAX(reps) FROM workout_sets WHERE exerciseId = :exerciseId AND weightLbs = :weight")
    LiveData<Integer> getMaxRepsAtWeight(int exerciseId, double weight);

    /**
     * Get max estimated 1RM for an exercise.
     */
    @Query("SELECT MAX(estimated1RM) FROM workout_sets WHERE exerciseId = :exerciseId")
    LiveData<Double> getMax1RMForExercise(int exerciseId);

    /**
     * Get total volume for an exercise.
     */
    @Query("SELECT SUM(volume) FROM workout_sets WHERE exerciseId = :exerciseId")
    LiveData<Double> getTotalVolumeForExercise(int exerciseId);

    /**
     * Get recent sets for an exercise (for progressive overload comparison).
     */
    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<WorkoutSet>> getRecentSetsForExercise(int exerciseId, int limit);

    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC LIMIT :limit")
    List<WorkoutSet> getRecentSetsForExerciseSync(int exerciseId, int limit);

    /**
     * Count sets for a session.
     */
    @Query("SELECT COUNT(*) FROM workout_sets WHERE sessionId = :sessionId")
    LiveData<Integer> getSetCountForSession(int sessionId);

    /**
     * Get average weight for an exercise.
     */
    @Query("SELECT AVG(weightLbs) FROM workout_sets WHERE exerciseId = :exerciseId")
    LiveData<Double> getAverageWeightForExercise(int exerciseId);

    /**
     * Get last set for an exercise (most recent).
     */
    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC LIMIT 1")
    LiveData<WorkoutSet> getLastSetForExercise(int exerciseId);

    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC LIMIT 1")
    WorkoutSet getLastSetForExerciseSync(int exerciseId);
}