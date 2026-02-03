package com.example.fitnessapp.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room Database implementation for the FitnessApp.
 * Version History:
 * - v1: Initial schema (users, weight_entries)
 * - v2: Previous updates
 * - v3: Changed password to passwordHash for BCrypt security
 * - v4: Added nutrition module (5 tables) + workout module (5 tables)
 */
@TypeConverters(Converters.class)
@Database(
        entities = {
                User.class,
                WeightEntry.class,
                // Nutrition Module (v4)
                Food.class,
                Meal.class,
                MealFood.class,
                DailyNutritionSummary.class,
                NutritionGoal.class,
                // Workout Module (v4)
                Exercise.class,
                WorkoutSession.class,
                WorkoutSet.class,
                PersonalRecord.class,
                WorkoutTemplate.class
        },
        version = 4,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // Define the abstract DAO accessors
    public abstract UserDao userDao();
    public abstract WeightEntryDao weightEntryDao();

    // Nutrition DAOs
    public abstract FoodDao foodDao();
    public abstract MealDao mealDao();
    public abstract MealFoodDao mealFoodDao();
    public abstract DailyNutritionSummaryDao dailyNutritionSummaryDao();
    public abstract NutritionGoalDao nutritionGoalDao();

    // Workout DAOs
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutSessionDao workoutSessionDao();
    public abstract WorkoutSetDao workoutSetDao();
    public abstract PersonalRecordDao personalRecordDao();
    public abstract WorkoutTemplateDao workoutTemplateDao();

    // Singleton instance and name
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "fitness_app_db";

    // Define the ExecutorService required by the Repository
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Migration from version 3 to version 4.
     * Adds nutrition module tables while preserving existing data.
     */
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // ===== NUTRITION MODULE TABLES =====

            // 1. Create foods table
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `foods` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`name` TEXT NOT NULL, " +
                            "`brand` TEXT, " +
                            "`servingSize` REAL NOT NULL, " +
                            "`calories` REAL NOT NULL, " +
                            "`protein` REAL NOT NULL, " +
                            "`carbs` REAL NOT NULL, " +
                            "`fats` REAL NOT NULL, " +
                            "`fiber` REAL NOT NULL, " +
                            "`sugar` REAL NOT NULL, " +
                            "`category` TEXT, " +
                            "`barcode` TEXT, " +
                            "`isCustom` INTEGER NOT NULL, " +
                            "`userId` INTEGER, " +
                            "`createdAt` INTEGER NOT NULL, " +
                            "FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
            );

            // Create indexes for foods table
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_foods_name` ON `foods` (`name`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_foods_category` ON `foods` (`category`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_foods_userId` ON `foods` (`userId`)");

            // 2. Create meals table
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `meals` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`userId` INTEGER NOT NULL, " +
                            "`mealType` TEXT NOT NULL, " +
                            "`date` TEXT NOT NULL, " +
                            "`timestamp` INTEGER NOT NULL, " +
                            "`notes` TEXT, " +
                            "`totalCalories` REAL NOT NULL, " +
                            "`totalProtein` REAL NOT NULL, " +
                            "`totalCarbs` REAL NOT NULL, " +
                            "`totalFats` REAL NOT NULL, " +
                            "FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
            );

            // Create indexes for meals table
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_meals_userId` ON `meals` (`userId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_meals_date` ON `meals` (`date`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_meals_userId_date` ON `meals` (`userId`, `date`)");

            // 3. Create meal_foods table (join table)
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `meal_foods` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`mealId` INTEGER NOT NULL, " +
                            "`foodId` INTEGER NOT NULL, " +
                            "`servings` REAL NOT NULL, " +
                            "`gramsConsumed` REAL NOT NULL, " +
                            "FOREIGN KEY(`mealId`) REFERENCES `meals`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, " +
                            "FOREIGN KEY(`foodId`) REFERENCES `foods`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)"
            );

            // Create indexes for meal_foods table
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_foods_mealId` ON `meal_foods` (`mealId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_foods_foodId` ON `meal_foods` (`foodId`)");

            // 4. Create daily_nutrition_summary table
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `daily_nutrition_summary` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`userId` INTEGER NOT NULL, " +
                            "`date` TEXT NOT NULL, " +
                            "`totalCalories` REAL NOT NULL, " +
                            "`totalProtein` REAL NOT NULL, " +
                            "`totalCarbs` REAL NOT NULL, " +
                            "`totalFats` REAL NOT NULL, " +
                            "`mealsLogged` INTEGER NOT NULL, " +
                            "`lastUpdated` INTEGER NOT NULL, " +
                            "FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
            );

            // Create indexes and unique constraint for daily_nutrition_summary
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_daily_nutrition_summary_userId` ON `daily_nutrition_summary` (`userId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_daily_nutrition_summary_date` ON `daily_nutrition_summary` (`date`)");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_daily_nutrition_summary_userId_date` ON `daily_nutrition_summary` (`userId`, `date`)");

            // 5. Create nutrition_goals table
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `nutrition_goals` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`userId` INTEGER NOT NULL, " +
                            "`height` REAL NOT NULL, " +
                            "`age` INTEGER NOT NULL, " +
                            "`gender` TEXT NOT NULL, " +
                            "`activityLevel` TEXT NOT NULL, " +
                            "`goal` TEXT NOT NULL, " +
                            "`bmr` REAL NOT NULL, " +
                            "`tdee` REAL NOT NULL, " +
                            "`calorieTarget` REAL NOT NULL, " +
                            "`proteinTarget` REAL NOT NULL, " +
                            "`carbsTarget` REAL NOT NULL, " +
                            "`fatsTarget` REAL NOT NULL, " +
                            "`waterTarget` REAL NOT NULL, " +
                            "`calculatedAt` INTEGER NOT NULL, " +
                            "FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
            );

            // Create unique index for nutrition_goals (one per user)
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_nutrition_goals_userId` ON `nutrition_goals` (`userId`)");

            // Log successful migration
            android.util.Log.d("AppDatabase", "Migration 3 → 4 complete: Added nutrition module (5 tables)");
        }
    };

    /**
     * Gets the singleton instance of the database
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME)
                            // TEMPORARY: Use destructive migration for development
                            .fallbackToDestructiveMigration()
                            // Callback for database events
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback for the database creation/opening process
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            android.util.Log.d("AppDatabase", "Database created - Version 4");
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            android.util.Log.d("AppDatabase", "Database opened - Version " + db.getVersion());
        }
    };
}