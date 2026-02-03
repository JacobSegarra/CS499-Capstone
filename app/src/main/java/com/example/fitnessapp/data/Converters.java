package com.example.fitnessapp.data;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Utility class containing TypeConverters to allow Room to handle non-primitive types,
 * specifically converting Date objects to and from Long timestamps,
 * and boolean to/from Integer for database compatibility.
 */
public class Converters {

    /**
     * Converts a Long timestamp (as stored in the database) back into a Date object.
     * @param value The Long timestamp read from the database.
     * @return The corresponding Date object, or null if the timestamp was null.
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Converts a Date object into a Long timestamp for storage in the database.
     * @param date The Date object to be stored.
     * @return The corresponding Long timestamp, or null if the date was null.
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Converts an Integer (as stored in the database) to a Boolean.
     * SQLite stores booleans as 0 (false) or 1 (true).
     * @param value The Integer value from database (0 or 1)
     * @return The corresponding Boolean, or null if value was null
     */
    @TypeConverter
    public static Boolean fromInt(Integer value) {
        if (value == null) {
            return null;
        }
        return value != 0;
    }

    /**
     * Converts a Boolean to an Integer for database storage.
     * @param value The Boolean value
     * @return 1 for true, 0 for false, null for null
     */
    @TypeConverter
    public static Integer booleanToInt(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? 1 : 0;
    }
}