# FitnessApp

A comprehensive Android fitness tracking application built with modern architecture patterns and security best practices. Originally developed as a basic weight tracker, now evolving into a full-featured fitness platform with nutrition and workout tracking capabilities.

## Current Features

### Weight Tracking
- Daily weight logging with timestamp
- Goal weight management
- Weight history visualization
- BMI calculation
- SMS notifications for goal achievement

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
- **v3**: Security enhancement (password → passwordHash)
- **v4+**: Planned expansion to 12+ tables (nutrition, workouts)

## Testing

### Manual Test Scenarios

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

### In Progress (Module 4 - Algorithms)
- [ ] Statistical analysis (moving averages, trend detection)
- [ ] BMI tracking and analysis
- [ ] Goal progress predictions
- [ ] Weight trend visualization

### Planned (Module 5 - Databases)
- [ ] Nutrition tracking module
  - Meal logging (breakfast, lunch, dinner, snacks)
  - Food database with calorie/macro information
  - Daily calorie tracking
  - Macro distribution (protein, carbs, fats)
- [ ] Workout tracking module
  - Exercise logging with sets/reps/weight
  - Personal record (PR) tracking
  - Progressive overload detection
  - 1RM calculations (Epley & Brzycki formulas)
- [ ] Database expansion (2 tables → 12+ tables)
- [ ] Proper database migrations (preserve user data)
- [ ] Database views for analytics
- [ ] Triggers for automatic calculations

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

## 👤 Author

**Jacob Segarra**
- University: Southern New Hampshire University
- Program: Computer Science BS
- Course: CS 499 - Computer Science Capstone

**Last Updated**: January 20, 2026  
**Version**: 3.0.0 (Module 3 Complete)  
**Status**: Active Development
