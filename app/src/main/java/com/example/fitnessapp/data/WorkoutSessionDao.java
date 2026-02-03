package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for WorkoutSession entity.
 */
@Dao
public interface WorkoutSessionDao {

    // INSERT
    @Insert
    long insert(WorkoutSession session);

    @Insert
    List<Long> insertAll(List<WorkoutSession> sessions);

    // UPDATE
    @Update
    void update(WorkoutSession session);

    // DELETE
    @Delete
    void delete(WorkoutSession session);

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId")
    void deleteById(int sessionId);

    // QUERIES

    /**
     * Get all sessions for a user.
     */
    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<WorkoutSession>> getAllSessionsForUser(int userId);

    /**
     * Get session by ID.
     */
    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId")
    LiveData<WorkoutSession> getSessionById(int sessionId);

    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId")
    WorkoutSession getSessionByIdSync(int sessionId);

    /**
     * Get sessions for a specific date.
     */
    @Query("SELECT * FROM workout_sessions WHERE userId = :userId AND date = :date ORDER BY timestamp ASC")
    LiveData<List<WorkoutSession>> getSessionsByDate(int userId, String date);

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId AND date = :date ORDER BY timestamp ASC")
    List<WorkoutSession> getSessionsByDateSync(int userId, String date);

    /**
     * Get sessions within a date range.
     */
    @Query("SELECT * FROM workout_sessions WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    LiveData<List<WorkoutSession>> getSessionsInRange(int userId, String startDate, String endDate);

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    List<WorkoutSession> getSessionsInRangeSync(int userId, String startDate, String endDate);

    /**
     * Get recent sessions.
     */
    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<WorkoutSession>> getRecentSessions(int userId, int limit);

    /**
     * Get total volume for a date range.
     */
    @Query("SELECT SUM(totalVolume) FROM workout_sessions WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalVolumeInRange(int userId, String startDate, String endDate);

    /**
     * Count sessions in date range.
     */
    @Query("SELECT COUNT(*) FROM workout_sessions WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    LiveData<Integer> getSessionCountInRange(int userId, String startDate, String endDate);

    /**
     * Get all dates with workouts.
     */
    @Query("SELECT DISTINCT date FROM workout_sessions WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<String>> getDatesWithWorkouts(int userId);

    /**
     * Get average session duration.
     */
    @Query("SELECT AVG(durationMinutes) FROM workout_sessions WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getAverageDurationInRange(int userId, String startDate, String endDate);
}