package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for PersonalRecord entity.
 */
@Dao
public interface PersonalRecordDao {

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonalRecord record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<PersonalRecord> records);

    // UPDATE
    @Update
    void update(PersonalRecord record);

    // DELETE
    @Delete
    void delete(PersonalRecord record);

    @Query("DELETE FROM personal_records WHERE id = :recordId")
    void deleteById(int recordId);

    // QUERIES

    /**
     * Get all PRs for a user.
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId ORDER BY dateAchieved DESC")
    LiveData<List<PersonalRecord>> getAllRecordsForUser(int userId);

    /**
     * Get PR by ID.
     */
    @Query("SELECT * FROM personal_records WHERE id = :recordId")
    LiveData<PersonalRecord> getRecordById(int recordId);

    @Query("SELECT * FROM personal_records WHERE id = :recordId")
    PersonalRecord getRecordByIdSync(int recordId);

    /**
     * Get all PRs for a specific exercise.
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId ORDER BY dateAchieved DESC")
    LiveData<List<PersonalRecord>> getRecordsForExercise(int userId, int exerciseId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId ORDER BY dateAchieved DESC")
    List<PersonalRecord> getRecordsForExerciseSync(int userId, int exerciseId);

    /**
     * Get specific PR type for an exercise.
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = :recordType")
    LiveData<PersonalRecord> getRecordByType(int userId, int exerciseId, String recordType);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = :recordType")
    PersonalRecord getRecordByTypeSync(int userId, int exerciseId, String recordType);

    /**
     * Get max weight PR for an exercise.
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = 'max_weight'")
    LiveData<PersonalRecord> getMaxWeightRecord(int userId, int exerciseId);

    /**
     * Get estimated 1RM PR for an exercise.
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = 'estimated_1rm'")
    LiveData<PersonalRecord> get1RMRecord(int userId, int exerciseId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = 'estimated_1rm'")
    PersonalRecord get1RMRecordSync(int userId, int exerciseId);

    /**
     * Get recent PRs (last N).
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId ORDER BY dateAchieved DESC LIMIT :limit")
    LiveData<List<PersonalRecord>> getRecentRecords(int userId, int limit);

    /**
     * Get PRs achieved in date range.
     */
    @Query("SELECT * FROM personal_records WHERE userId = :userId AND dateAchieved BETWEEN :startDate AND :endDate ORDER BY dateAchieved DESC")
    LiveData<List<PersonalRecord>> getRecordsInRange(int userId, String startDate, String endDate);

    /**
     * Count total PRs for user.
     */
    @Query("SELECT COUNT(*) FROM personal_records WHERE userId = :userId")
    LiveData<Integer> getTotalRecordCount(int userId);

    /**
     * Count PRs for specific exercise.
     */
    @Query("SELECT COUNT(*) FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId")
    LiveData<Integer> getRecordCountForExercise(int userId, int exerciseId);

    /**
     * Check if PR exists for exercise and type.
     */
    @Query("SELECT COUNT(*) FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = :recordType")
    int checkRecordExists(int userId, int exerciseId, String recordType);

    /**
     * Get all record types for an exercise.
     */
    @Query("SELECT DISTINCT recordType FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId")
    LiveData<List<String>> getRecordTypesForExercise(int userId, int exerciseId);
}