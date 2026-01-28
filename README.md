FitnessApp

A comprehensive Android fitness tracking application built with modern architecture patterns and security best practices. Originally developed as a basic weight tracker, now evolving into a full-featured fitness platform with nutrition and workout tracking capabilities.

## Current Features

### Weight Tracking & Analysis
- Daily weight logging with timestamp
- **Intelligent trend detection** (losing, gaining, maintaining)
- **Moving averages** (7-day and 30-day smoothing)
- **Weight predictions** using linear regression
- **Days-to-goal calculator** based on current trend
- Goal weight management with progress tracking
- BMI calculation and categorization
- SMS notifications for goal achievement

### Nutrition Intelligence
- **BMR calculation** using Mifflin-St Jeor equation
- **TDEE calculation** with activity level multipliers
- **Personalized calorie targets** for cutting, bulking, or maintenance
- **Macro distribution** (protein, carbs, fats) tailored to fitness goals
- **Water intake recommendations** based on body weight and activity
- Safety minimums enforced (1500 cal male, 1200 cal female)

### Workout Analysis
- **1RM estimation** using Epley and Brzycki formulas (averaged for accuracy)
- **Progressive overload detection** (2.5% volume threshold)
- **Strength level assessment** (Beginner to Elite relative to bodyweight)
- **Training weight calculator** for different rep ranges
- **Volume tracking** across workout sessions
- **Intensity calculations** (percentage of 1RM)

### User Management
- Secure user authentication with BCrypt password hashing
- Input validation for all user data
- Persistent local storage with Room database
- Settings management (goal weight, phone number)

### Security & Architecture
- MVVM (Model-View-ViewModel) architecture
- BCrypt password hashing (12-round cost factor)
- Comprehensive input validation framework
- Structured error handling with Result pattern
- SQL injection prevention

## Architecture

### Design Patterns
- **MVVM Architecture**: Complete separation of UI and business logic
- **Repository Pattern**: Single source of truth for data access
- **Observer Pattern**: LiveData for reactive UI updates
- **Factory Pattern**: ViewModelFactory for dependency injection

### Project Structure
com.example.fitnessapp/
├── data/                    # Room database entities and DAOs
│   ├── User.java
│   ├── WeightEntry.java
│   ├── UserDao.java
│   ├── WeightEntryDao.java
│   └── AppDatabase.java
├── repository/              # Data access layer
│   └── EntryRepository.java
├── viewmodel/               # Business logic layer
│   ├── BaseViewModel.java
│   ├── LoginViewModel.java
│   ├── RegistrationViewModel.java
│   ├── SettingsViewModel.java
│   ├── EntryViewModel.java
│   └── (ViewModelFactories)
├── util/                    # Utility classes
│   ├── Result.java          # Error handling wrapper
│   ├── SecurityUtils.java   # BCrypt operations
│   └── ValidationUtils.java # Input validation
├── ui/                      # UI components
├── adapter/                 # RecyclerView adapters
└── (Activities)             # UI controllers

## Security Features

### Password Security
- **BCrypt Hashing**: Industry-standard password hashing with salt
- **No Plain-Text Storage**: Passwords never stored in readable format
- **Secure Verification**: Password comparison without decryption

### Input Validation
- **Username**: 4-20 alphanumeric characters and underscores
- **Password**: Minimum 8 characters, requires uppercase, lowercase, and number
- **Phone Number**: 10-digit US format validation
- **Weight**: Range validation (0-500 kg) to prevent invalid data

### Data Protection
- **SQL Injection Prevention**: Parameterized queries throughout
- **Error Message Sanitization**: No internal details exposed to users
- **Multi-Layer Validation**: Entity, DAO, and Repository-level checks

## Algorithm Suite (Module 4)

### Statistical Analysis (O(n) complexity)
- **Moving Averages**: Configurable window sizes (7-day, 30-day) smooth daily fluctuations
- **Linear Regression**: Least squares method calculates weight change rate
- **Trend Detection**: Classifies as losing, gaining, or maintaining (0.2 kg/week threshold)
- **Weight Prediction**: Forecasts future weight based on current trend
- **Days to Goal**: Calculates timeline to reach target weight
- **Standard Deviation**: Measures consistency in weight tracking

### Nutrition Calculations (O(1) complexity)
- **BMR (Basal Metabolic Rate)**: Mifflin-St Jeor equation
  - Formula: `BMR = (10 × weight_kg) + (6.25 × height_cm) - (5 × age) + s`
  - Source: Mifflin et al. (1990), American Journal of Clinical Nutrition
  - Accuracy: ±10% for most individuals
- **TDEE (Total Daily Energy Expenditure)**: BMR × activity multiplier
  - Sedentary (1.2), Light (1.375), Moderate (1.55), Active (1.725), Very Active (1.9)
- **Calorie Targets**: 
  - Cutting: TDEE - 500 cal (~0.5 kg/week loss)
  - Bulking: TDEE + 300 cal (~0.3 kg/week gain)
  - Maintenance: TDEE
- **Macro Distribution**: Protein/Carbs/Fats percentages based on fitness goals
  - Cutting: 40/30/30 (high protein preserves muscle)
  - Bulking: 30/50/20 (high carbs fuel growth)
  - Maintenance: 30/40/30 (balanced)
- **BMI Calculation**: `weight(kg) / height(m)²` with WHO categories
- **Water Intake**: 30-35ml per kg bodyweight, adjusted for activity level

### Workout Analysis (O(1) complexity)
- **1RM Estimation**: 
  - **Epley Formula**: `1RM = weight × (1 + reps/30)` (best for 1-10 reps)
  - **Brzycki Formula**: `1RM = weight × (36/(37-reps))` (best for 2-10 reps)
  - Implementation averages both formulas for improved accuracy
- **Progressive Overload Detection**: 2.5% volume increase threshold based on sports science
- **Volume Calculation**: `weight × reps × sets` tracked across sessions
- **Strength Level Assessment**: Categorizes lifts relative to bodyweight
  - Beginner (<1.0×), Intermediate (1.0-1.5×), Advanced (1.5-2.0×), Elite (>2.0×)
- **Training Recommendations**: Calculates appropriate weights for target rep ranges
- **Intensity Tracking**: Percentage of 1RM for programming training cycles

### Scientific Validation
All algorithms use peer-reviewed, validated formulas:
- Mifflin-St Jeor equation (1990) - most accurate modern BMR formula
- Epley formula (1985) - validated 1RM estimation
- Brzycki formula (1993) - 1RM estimation with ±3% accuracy for 2-10 reps
- Linear regression (least squares method) - standard statistical approach

## Technologies & Libraries

### Core Technologies
- **Language**: Java
- **Platform**: Android SDK 24+ (Android 7.0 Nougat)
- **Build Tool**: Gradle (Kotlin DSL)

## Database Schema

### Current Tables (v3)
- **user_table**: User credentials and settings
  - `id` (PK, autoincrement)
  - `username` (unique, indexed)
  - `passwordHash` (BCrypt hashed)
  - `goalWeight` (double)
  - `phoneNumber` (string)

- **weight_entry_table**: Weight measurements
  - `id` (PK, autoincrement)
  - `userId` (FK → user_table, CASCADE delete)
  - `weight` (double)
  - `timestamp` (long)

### Database Evolution
- **v1**: Initial schema
- **v2**: Previous updates
- **v3**: Security enhancement (BCrypt password hashing)
- **v5+**: Planned expansion to 12+ tables (nutrition, workouts)

## Testing
### Algorithm Testing
All algorithms tested and verified:
- **Statistical Analysis**: 30-day sample data correctly analyzed
  - Moving averages calculated accurately
  - Trend detection: "Losing 0.4 kg/week"
  - Predictions: 30-day forecast within expected range
  - Days to goal: Calculated correctly based on trend
- **Nutrition Calculations**: Validated against online calculators
  - BMR: 1840 cal/day (matches reference calculators)
  - TDEE: 2852 cal/day (moderate activity)
  - Macros: Sum correctly to calorie target
- **Workout Analysis**: Validated against published strength tables
  - 1RM: 114kg estimated from 100kg × 5 reps
  - Progressive overload: Detected +4.2% volume increase
  - Strength level: Correctly categorized as "Intermediate"

**Registration Validation:**

✓ Try username "ab" → Should fail (too short)
✓ Try password "password" → Should fail (no uppercase/number)
✓ Try password "Password123" → Should succeed
✓ Register same username twice → Should fail (duplicate)

**Authentication:**

✓ Login with correct credentials → Should succeed
✓ Login with wrong password → Should fail with error message
✓ Login with non-existent user → Should fail

**Settings Update:**

✓ Update goal weight to 75.5 → Should save
✓ Try negative weight → Should fail validation
✓ Update phone to "1234567890" → Should succeed

### Unit Testing (Planned)
- ViewModel business logic tests
- Repository authentication tests
- Validation framework tests

## Roadmap

### Completed (Module 3)
- [x] MVVM architecture implementation
- [x] BCrypt password security
- [x] Input validation framework
- [x] Structured error handling
- [x] Repository pattern with authentication
- [x] Project renamed to FitnessApp

### Completed (Module 4 - Algorithms)
- [x] Statistical analysis suite (moving averages, linear regression, predictions)
- [x] Nutrition calculation suite (BMR, TDEE, macros, BMI)
- [x] Workout analysis suite (1RM formulas, progressive overload, volume tracking)
- [x] Scientific formula validation with peer-reviewed sources
- [x] Comprehensive algorithm testing (all methods operational)
- [x] Algorithm complexity documentation (Big-O notation)
- [x] Sample data generator for testing

### In Progress (Module 5 - Databases)
- [ ] Expand database from 2 tables to 12+ tables
- [ ] **Nutrition Module** (4-5 tables):
  - `foods` - Food database with calories/macros
  - `meals` - Meal records by date
  - `meal_foods` - Join table for foods in meals
  - `daily_nutrition` - Auto-calculated daily totals
  - `nutrition_goals` - Store calculated targets from algorithms
- [ ] **Workout Module** (4-5 tables):
  - `exercises` - Exercise database
  - `workout_sessions` - Workout records
  - `workout_sets` - Individual sets with weight/reps
  - `personal_records` - Auto-tracked PRs
  - `workout_templates` - Saved routines
- [ ] **Analytics Module** (2-3 tables):
  - `weekly_summaries` - Aggregated weekly stats
  - `goal_milestones` - Achievement tracking
- [ ] Implement proper database migrations (preserve user data)
- [ ] Create database triggers for auto-calculations
- [ ] Design database views for complex queries
- [ ] Add foreign key relationships with CASCADE
- [ ] Create indexes for query optimization
- [ ] Build nutrition logging UI
- [ ] Build workout logging UI
- [ ] Integrate algorithms with real user data

### Future Enhancements
- [ ] Data visualization with charts/graphs
- [ ] Cloud sync and backup
- [ ] Multi-user support
- [ ] Export data (CSV, PDF)
- [ ] Dark mode support
- [ ] Internationalization (i18n)

## Development Notes

### Breaking Changes (v3)
**Database schema migration from v2 to v3 requires app reinstall**
- All existing data will be deleted
- Users must re-register with new BCrypt-hashed passwords
- Proper migrations will be implemented in future versions

### Known Issues
- No password reset functionality (planned)
- No "Remember Me" feature (planned)
- SMS permissions must be granted manually


This project is developed for academic purposes as part of CS 499 Capstone.

## Author

**Jacob Segarra**
- University: Southern New Hampshire University
- Program: Computer Science BS
- Course: CS 499 - Computer Science Capstone
- GitHub: https://jacobsegarra.github.io/CS499-Capstone/

**Last Updated**: January 27, 2026  
**Version**: 3.0.0 
**Status**: Active Development

**Last Updated**: January 20, 2026  
**Version**: 3.0.0 (Module 4 Complete)  
**Status**: Active Development
