package com.example.optiononeweighttrackingappjacobsegarra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.optiononeweighttrackingappjacobsegarra.data.AppDatabase; // Needed for the Executor
import com.example.optiononeweighttrackingappjacobsegarra.repository.EntryRepository;
import com.example.optiononeweighttrackingappjacobsegarra.data.User;
import com.example.optiononeweighttrackingappjacobsegarra.databinding.ActivityLoginBinding;

import java.util.List;

/**
 * Handles user login and navigation to the dedicated RegistrationActivity.
 * Database calls are executed on a background thread to prevent ANRs.
 */
public class LoginActivity extends AppCompatActivity {

    // NOTE: Ensure your build.gradle (app) includes viewBinding = true
    private ActivityLoginBinding binding;
    private EntryRepository entryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the Repository (used for database operations)
        entryRepository = new EntryRepository(getApplication());

        setupListeners();
    }

    private void setupListeners() {
        // Login Button Logic
        binding.btnLogin.setOnClickListener(v -> handleLogin());

        // Register LINK Logic (Navigates to the dedicated registration screen)
        // Uses the ID 'tvRegister' from activity_login.xml
        binding.tvRegister.setOnClickListener(v -> navigateToRegistration());
    }

    // --- Login Logic using Room Repository (Corrected Threading) ---
    private void handleLogin() {
        // Use the IDs from your activity_login.xml: edit_text_username and edit_text_password
        String username = binding.editTextUsername.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Execute database query on the background thread
        // NOTE: We use AppDatabase.databaseWriteExecutor as defined in the AppDatabase class
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Synchronous call on background thread is safe
            // We now use the Repository's new method: authenticateUser(username, password)
            User user = entryRepository.authenticateUser(username, password);

            // Post result back to the main (UI) thread
            runOnUiThread(() -> {
                if (user != null) {
                    // Login Successful!
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startMainActivity(user.getId());
                } else {
                    // User was not found or password did not match
                    Toast.makeText(this, "Login Failed: Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // --- Navigation to Registration ---
    private void navigateToRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the MAIN Activity upon successful authentication.
     * @param userId The unique ID of the logged-in user.
     */
    private void startMainActivity(int userId) {
        Intent intent = new Intent(this, MainActivity.class);
        // Pass the user ID to the main screen
        intent.putExtra(MainActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
        finish();
    }
}