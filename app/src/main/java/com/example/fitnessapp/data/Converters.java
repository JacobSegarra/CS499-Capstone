package com.example.fitnessapp.data;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Utility class containing TypeConverters to allow Room to handle non-primitive types,
 * specifically converting Date objects to and from Long timestamps.
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
}
