# FitnessApp

A comprehensive Android fitness tracking application demonstrating professional software architecture, scientifically-validated algorithms, and production-grade database design. Developed as a capstone project showcasing transformation from a basic weight tracker to an intelligent fitness platform.

[![GitHub](https://img.shields.io/badge/GitHub-Profile-blue)](https://github.com/jacobsegarra)
[![Portfolio](https://img.shields.io/badge/ePortfolio-View-purple)](https://jacobsegarra.github.io/CS499-Capstone/)
[![Status](https://img.shields.io/badge/Status-Complete-success)](https://github.com/jacobsegarra/CS499-Capstone)

## Project Overview

**Academic Context**: CS 499 Computer Science Capstone | Southern New Hampshire University  
**Timeline**: January - February 2026  
**Status**: All Modules Complete

This project demonstrates comprehensive computer science competencies through systematic enhancement across three domains:
- **Module 3**: Software Design & Engineering (MVVM architecture, BCrypt security)
- **Module 4**: Algorithms & Data Structures (25+ scientifically-validated algorithms)
- **Module 5**: Databases (12-table normalized schema with Room)

---

## Features

### Weight Tracking & Analysis
- Daily weight logging with intelligent trend detection
- Moving averages (7-day and 30-day) using O(n×w) sliding window algorithm
- Weight predictions via linear regression (least squares method, O(n) complexity)
- Days-to-goal calculator with 0.2 kg/week threshold
- BMI calculation with WHO categorization
- Standard deviation tracking for consistency metrics
- Unit conversion system (pounds ↔ kilograms)

### Nutrition Intelligence
- **BMR** calculation using Mifflin-St Jeor equation (±10% accuracy)
- **TDEE** calculation with activity level multipliers (Sedentary 1.2 → Very Active 1.9)
- **Personalized calorie targets**: Cutting (-500 cal), Bulking (+300 cal), Maintenance
- **Macro distribution** tailored to fitness goals (Cutting: 40/30/30, Bulking: 30/50/20)
- **Water intake** recommendations (30-35ml per kg, activity-adjusted)
- Safety minimums enforced (1500 cal male, 1200 cal female)

### Workout Analysis
- **1RM estimation** using averaged Epley + Brzycki formulas for improved accuracy
- **Progressive overload detection** with 2.5% volume threshold (sports science-based)
- **Strength level assessment** relative to bodyweight (Beginner → Elite)
- **Training weight calculator** for different rep ranges (3, 8, 12+ reps)
- **Volume tracking** (weight × reps × sets) across sessions
- **Intensity calculations** (percentage of 1RM) for programming

### Security & Architecture
- **MVVM Architecture**: Complete separation of concerns for maintainability
- **BCrypt Password Hashing**: 12-round cost factor, industry-standard security
- **Input Validation Framework**: Multi-layer validation (UI, ViewModel, Repository)
- **Type-Safe Error Handling**: Result<T> pattern prevents crashes
- **SQL Injection Prevention**: Parameterized queries throughout
- **Defense-in-Depth**: Error message sanitization, cascade deletion constraints

---

## Architecture

### Design Patterns Implemented
- **MVVM (Model-View-ViewModel)**: Enables parallel development and testability
- **Repository Pattern**: Single source of truth for data access
- **Observer Pattern**: LiveData for reactive UI updates
- **Facade Pattern**: AlgorithmService for complex calculations
- **Factory Pattern**: ViewModelFactory for dependency injection
- **DAO Pattern**: Type-safe database access with Room

### Project Structure
```
com.example.fitnessapp/
├── data/                    # Room database (12 tables)
│   ├── entities/           # User, WeightEntry, Food, Meal, Exercise, etc.
│   ├── dao/                # 12 DAO interfaces (100+ query methods)
│   └── AppDatabase.java
├── algorithm/              # 25+ algorithm methods
│   ├── StatisticalAnalyzer.java
│   ├── NutritionCalculator.java
│   └── WorkoutAnalyzer.java
├── repository/             # Data access layer
│   └── EntryRepository.java
├── viewmodel/              # Business logic (5 ViewModels)
│   ├── BaseViewModel.java
│   ├── LoginViewModel.java
│   ├── RegistrationViewModel.java
│   ├── SettingsViewModel.java
│   └── EntryViewModel.java
├── util/                   # Utility classes
│   ├── Result.java         # Error handling wrapper
│   ├── SecurityUtils.java  # BCrypt operations
│   ├── ValidationUtils.java # Input validation
│   └── UnitConverter.java  # Pounds ↔ Kilograms
├── ui/                     # Activities and fragments
└── adapter/                # RecyclerView adapters
```

---

## Database Architecture

### Schema Overview (12 Tables, Third Normal Form)

**Original Database (v3)**:
- `users` (2 tables)
- `weight_entries`

**Enhanced Database (v5)** - 12 Tables:

**Nutrition Module** (5 tables):
- `foods` - Reusable food database (calories, protein, carbs, fats, fiber, sugar)
- `meals` - Meal records by date and type (breakfast, lunch, dinner, snack)
- `meal_foods` - Many-to-many join table (meal ↔ foods with serving sizes)
- `daily_nutrition_summary` - Auto-calculated daily totals
- `nutrition_goals` - Stores BMR, TDEE, macro targets from algorithms

**Workout Module** (5 tables):
- `exercises` - Exercise database with categories and equipment types
- `workout_sessions` - Workout records by date with total volume
- `workout_sets` - Individual sets (weight, reps, estimated 1RM)
- `personal_records` - Auto-tracked PRs (max weight, max reps, max 1RM)
- `workout_templates` - Saved workout routines for quick logging

**Core Tables** (2 tables - preserved):
- `users` - User credentials (BCrypt hashed), goals, settings
- `weight_entries` - Weight measurements with timestamps

### Database Features
- **Normalization**: Third Normal Form (3NF) eliminates data redundancy
- **Foreign Key Constraints**: CASCADE for user data, RESTRICT for protected relationships
- **Composite Indexes**: Optimized for common queries (userId, date)
- **100+ DAO Methods**: Type-safe database access with LiveData integration
- **TypeConverters**: Proper handling of boolean and date types in SQLite
- **Room Persistence Library**: Compile-time SQL verification prevents runtime errors

---

## Algorithm Suite

### Statistical Analysis (O(n) complexity)
- **Moving Averages**: 7-day and 30-day smoothing with sliding window
- **Linear Regression**: Least squares method for weight trend analysis
- **Trend Detection**: Classifies as losing/gaining/maintaining (0.2 kg/week threshold)
- **Weight Prediction**: Forecasts future weight based on historical patterns
- **Days to Goal**: Timeline calculation for target weight achievement

### Nutrition Calculations (O(1) complexity)
- **Mifflin-St Jeor Equation** (1990):  
  `BMR = (10 × weight_kg) + (6.25 × height_cm) - (5 × age) + s`  
  *Source: Mifflin et al., American Journal of Clinical Nutrition*
- **TDEE**: BMR × activity multiplier (1.2 to 1.9)
- **Macro Distribution**: Evidence-based ratios for cutting, bulking, maintenance
- **BMI**: `weight(kg) / height(m)²` with WHO categories
- **Water Intake**: 30-35ml per kg bodyweight, activity-adjusted

### Workout Analysis (O(1) complexity)
- **Epley Formula** (1985): `1RM = weight × (1 + reps/30)` - best for 1-10 reps
- **Brzycki Formula** (1993): `1RM = weight × (36/(37-reps))` - best for 2-10 reps  
  *Implementation averages both for improved accuracy across rep ranges*
- **Progressive Overload**: 2.5% volume threshold based on sports science research
- **Strength Assessment**: Categorizes relative to bodyweight (Beginner to Elite)
- **Volume Tracking**: `weight × reps × sets` across sessions

### Scientific Validation
All formulas peer-reviewed and validated:
- Mifflin-St Jeor (1990) - most accurate modern BMR formula
- Epley (1985) - validated 1RM estimation
- Brzycki (1993) - ±3% accuracy for 2-10 reps
- Linear regression - least squares standard method

**Complexity Analysis**: All algorithms documented with Big-O notation in Javadoc

---

## Security Implementation

### Password Security
- **BCrypt Hashing**: Industry-standard with salt, 12-round cost factor
- **No Plain-Text Storage**: Passwords never stored in readable format
- **Secure Verification**: One-way hash comparison

### Input Validation
- **Username**: 4-20 alphanumeric characters and underscores
- **Password**: 8+ characters, requires uppercase, lowercase, number
- **Phone**: 10-digit US format
- **Weight**: 0-500 kg range validation

### Data Protection
- **SQL Injection Prevention**: Parameterized queries throughout
- **Error Message Sanitization**: No internal details exposed
- **Multi-Layer Validation**: UI → ViewModel → Repository
- **Referential Integrity**: Foreign key constraints with CASCADE/RESTRICT
- **Data Isolation**: userId filtering ensures users only access own data

---

## Technical Stack

### Core Technologies
- **Language**: Java
- **Platform**: Android SDK 24+ (Android 7.0 Nougat)
- **Architecture**: MVVM with LiveData
- **Database**: Room Persistence Library (SQLite)
- **Build Tool**: Gradle (Kotlin DSL)
- **Security**: BCrypt (jBCrypt library)

### Code Metrics
- **Java Files**: 47+
- **Database Tables**: 12
- **Entities**: 12
- **DAOs**: 12 (100+ methods)
- **Algorithm Methods**: 25+
- **ViewModels**: 5 (Base + 4 domain-specific)

---

## Course Outcomes Demonstrated

### CS 499 Computer Science Capstone - Southern New Hampshire University

**Outcome 1: Collaborative Environments**  
MVVM architecture enables parallel development. Comprehensive documentation (Javadoc, narratives, code review video) supports team collaboration. GitHub repository demonstrates professional version control practices.

**Outcome 2: Professional Communication**  
10,494 words of technical narratives, 40-minute code review video, algorithm complexity documented with Big-O notation, peer-reviewed formula citations, comprehensive ePortfolio.

**Outcome 3: Design Solutions**  
Evaluated trade-offs: BCrypt cost factors (security vs. performance), validation strictness (security vs. UX), normalization (data integrity vs. query performance).

**Outcome 4: Well-Founded Techniques**  
Scientifically-validated formulas (Mifflin-St Jeor, Epley, Brzycki), Third Normal Form database design, industry-standard patterns (MVVM, Repository, DAO).

**Outcome 5: Security Mindset**  
Defense-in-depth validation, BCrypt hashing, SQL injection prevention, parameterized queries, referential integrity constraints, error message sanitization, cascade deletion for data cleanup.

---

## Documentation

### Available Resources
- **[ePortfolio](https://jacobsegarra.github.io/CS499-Capstone/)** - Professional portfolio website with collapsible narratives
- **[Code Review Video](https://www.youtube.com/watch?v=fHbl39XfF_Y)** - 40-minute technical walkthrough
- **Enhancement Narratives**:
  - Module 3: Software Design
  - Module 4: Algorithms
  - Module 5: Databases
- **Professional Self-Assessment**

### Project Timeline
- **January 20, 2026**: Module 3 Complete - MVVM Architecture & BCrypt Security
- **January 27, 2026**: Module 4 Complete - Algorithm Suite (25+ methods)
- **February 3, 2026**: Module 5 Complete - Database Expansion (12 tables, 100+ DAO methods)
- **Status**: All Three Technical Modules Complete

---

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK 24+ (Android 7.0 Nougat)

### Installation
```bash
# Clone the repository
git clone https://github.com/jacobsegarra/CS499-Capstone.git

# Open in Android Studio
# Build and run on emulator or device
```

### First Launch
1. Register a new user account (password will be BCrypt hashed)
2. Set your goal weight in Settings
3. Start logging daily weights
4. Explore nutrition and workout calculations

---

## Testing

### Algorithm Verification
 **Statistical Analysis**: 30-day sample data correctly analyzed  
 **Nutrition Calculations**: Validated against reference calculators  
 **Workout Analysis**: Validated against published strength tables  
 **Database Integration**: All 12 tables operational with 100+ DAO methods  
 **Security**: BCrypt hashing, input validation, SQL injection prevention

### Manual Testing Performed
- Registration with various invalid inputs (validated)
- Authentication with correct/incorrect credentials (validated)
- Weight logging and trend analysis (validated)
- Algorithm calculations across edge cases (validated)
- Database migrations and data integrity (validated)

---

## Learning Outcomes

### Key Insights from Development

**Architecture**: Separating concerns isn't just good practice—it's essential for maintainability. MVVM transformed a 150-line Activity into 60 lines of clean UI code with testable ViewModels.

**Algorithms**: Real-world algorithms require domain expertise, not just computational knowledge. Averaging Epley and Brzycki formulas improves accuracy across rep ranges—a lesson in practical vs. theoretical correctness.

**Databases**: Normalization is a balance, not an absolute. Storing pre-calculated totals (denormalization) trades storage for query performance when historical data is read frequently.

**Security**: Defense-in-depth means layered protection. BCrypt + validation + parameterized queries + error sanitization ensures no single failure compromises security.

**Most Valuable**: Software architecture is about managing complexity and enabling change. Good architecture has higher upfront cost but pays dividends when adding features, fixing bugs, or collaborating with teams.

---

## Author

**Jacob Segarra**  
Computer Science BS | Southern New Hampshire University  
CS 499 Computer Science Capstone | 2026

**Links**:
- [GitHub Profile](https://github.com/jacobsegarra)
- [ePortfolio](https://jacobsegarra.github.io/CS499-Capstone/)
- [Code Review Video](https://www.youtube.com/watch?v=fHbl39XfF_Y)

---

## Acknowledgments

- **Mifflin et al. (1990)** - BMR equation validation research
- **Epley (1985)** & **Brzycki (1993)** - 1RM formula research
- **SNHU CS Program** - Comprehensive computer science education
- **Android Documentation** - Official guides and best practices
- **jBCrypt Library** - Secure password hashing implementation

---

**Last Updated**: February 17, 2026  
**Version**: 5.0.0 (Capstone Complete)  
**Status**:  Project Complete - All Modules Delivered

---

<div align="center">

**🎓 CS 499 Computer Science Capstone | Southern New Hampshire University | 2026**

*Transforming a basic weight tracker into a fitness platform*

[View ePortfolio](https://jacobsegarra.github.io/CS499-Capstone/) • [Watch Code Review](https://www.youtube.com/watch?v=fHbl39XfF_Y) • [GitHub Profile](https://github.com/jacobsegarra)

</div>
