package com.example.fitnessapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO for WorkoutTemplate entity.
 */
@Dao
public interface WorkoutTemplateDao {

    // INSERT
    @Insert
    long insert(WorkoutTemplate template);

    @Insert
    List<Long> insertAll(List<WorkoutTemplate> templates);

    // UPDATE
    @Update
    void update(WorkoutTemplate template);

    // DELETE
    @Delete
    void delete(WorkoutTemplate template);

    @Query("DELETE FROM workout_templates WHERE id = :templateId")
    void deleteById(int templateId);

    // QUERIES

    /**
     * Get all templates for a user.
     */
    @Query("SELECT * FROM workout_templates WHERE userId = :userId ORDER BY templateName ASC")
    LiveData<List<WorkoutTemplate>> getAllTemplatesForUser(int userId);

    /**
     * Get template by ID.
     */
    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
    LiveData<WorkoutTemplate> getTemplateById(int templateId);

    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
    WorkoutTemplate getTemplateByIdSync(int templateId);

    /**
     * Get templates sorted by last used.
     */
    @Query("SELECT * FROM workout_templates WHERE userId = :userId ORDER BY lastUsed DESC")
    LiveData<List<WorkoutTemplate>> getTemplatesByLastUsed(int userId);

    /**
     * Get recently used templates.
     */
    @Query("SELECT * FROM workout_templates WHERE userId = :userId AND lastUsed IS NOT NULL ORDER BY lastUsed DESC LIMIT :limit")
    LiveData<List<WorkoutTemplate>> getRecentlyUsedTemplates(int userId, int limit);

    /**
     * Search templates by name.
     */
    @Query("SELECT * FROM workout_templates WHERE userId = :userId AND templateName LIKE '%' || :searchQuery || '%' ORDER BY templateName ASC")
    LiveData<List<WorkoutTemplate>> searchTemplatesByName(int userId, String searchQuery);

    /**
     * Count templates for user.
     */
    @Query("SELECT COUNT(*) FROM workout_templates WHERE userId = :userId")
    LiveData<Integer> getTemplateCount(int userId);

    /**
     * Update last used timestamp.
     */
    @Query("UPDATE workout_templates SET lastUsed = :timestamp WHERE id = :templateId")
    void updateLastUsed(int templateId, long timestamp);
}