package com.example.optiononeweighttrackingappjacobsegarra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.optiononeweighttrackingappjacobsegarra.data.AppDatabase;
import com.example.optiononeweighttrackingappjacobsegarra.repository.EntryRepository;
import com.example.optiononeweighttrackingappjacobsegarra.data.User;
import com.example.optiononeweighttrackingappjacobsegarra.databinding.ActivityRegistrationBinding;

import java.util.List;

/**
 * Dedicated activity for new user registration. It collects all required data
 * and uses the EntryRepository to insert the new user on a background thread.
 */
public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private EntryRepository entryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Setup View Binding
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Initialize the Repository
        entryRepository = new EntryRepository(getApplication());

        // 3. Setup Toolbar for Back Navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.btn_register));
        }

        // 4. Setup Listener
        binding.btnCompleteRegistration.setOnClickListener(v -> handleRegistration());
    }

    // Handles the back button press in the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Handles input validation, username check, and new user insertion.
     */
    private void handleRegistration() {
        String username = binding.editTextUsernameReg.getText().toString().trim();
        String password = binding.editTextPasswordReg.getText().toString().trim();
        String goalText = binding.editTextGoalWeightReg.getText().toString().trim();
        String phone = binding.editTextPhoneNumberReg.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || goalText.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validate phone number format (basic check)
        if (phone.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number (min 10 digits).", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            double goalWeight = Double.parseDouble(goalText);

            // Execute database check/insert on the background thread
            AppDatabase.databaseWriteExecutor.execute(() -> {
                // 1. Check if username already exists (blocking call)
                List<User> existingUser = entryRepository.getUserByUsername(username);

                // Post result back to the main (UI) thread
                runOnUiThread(() -> {
                    if (existingUser != null && !existingUser.isEmpty()) {
                        Toast.makeText(this, "Registration Failed: Username already exists.", Toast.LENGTH_LONG).show();
                    } else {
                        // 2. All checks passed, proceed with user insertion on background thread
                        insertNewUser(username, password, goalWeight, phone);
                    }
                });
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Goal Weight format. Must be a number.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Executes user insertion on the background thread and transitions to MainActivity on UI thread.
     */
    private void insertNewUser(String username, String password, double goalWeight, String phone) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User newUser = new User(username, password, goalWeight, phone);
            // Insert the user (blocking call)
            long newUserId = entryRepository.insertUser(newUser);

            runOnUiThread(() -> {
                if (newUserId > 0) {
                    Toast.makeText(this, "Registration Successful! Logged in.", Toast.LENGTH_LONG).show();
                    // Pass the newly created ID to the main screen
                    startMainActivity((int) newUserId);
                } else {
                    Toast.makeText(this, "Registration Failed. Database insertion failed.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    /**
     * Navigates to the MAIN Activity upon successful registration.
     */
    private void startMainActivity(int userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
        // User has successfully registered and logged in, finish all previous activities
        finishAffinity();
    }
}