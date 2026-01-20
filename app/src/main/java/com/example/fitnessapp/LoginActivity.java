package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.viewmodel.LoginViewModel;

/**
 * Handles user login using LoginViewModel
 * Now uses proper MVVM architecture with ViewModel and LiveData
 */
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    // UI Components (using actual IDs from activity_login.xml)
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Find views with CORRECT IDs
        initializeViews();

        setupObservers();
        setupListeners();
    }

    /**
     * Initializes all view references with correct IDs from XML.
     */
    private void initializeViews() {
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
    }

    /**
     * Sets up LiveData observers for ViewModel state changes.
     */
    private void setupObservers() {
        // Observe authentication result
        viewModel.authenticatedUser.observe(this, user -> {
            if (user != null) {
                // Login successful - navigate to MainActivity
                startMainActivity(user.getId());
                viewModel.clearAuthenticatedUser();
            }
        });

        // Observe error messages
        viewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                viewModel.clearError();
            }
        });

        // Observe loading state
        viewModel.isLoading.observe(this, isLoading -> {
            if (btnLogin != null) {
                btnLogin.setEnabled(!isLoading);
            }
        });
    }

    /**
     * Sets up button click listeners.
     */
    private void setupListeners() {
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> handleLogin());
        }

        if (tvRegister != null) {
            tvRegister.setOnClickListener(v -> navigateToRegistration());
        }
    }

    /**
     * Handles login button click.
     */
    private void handleLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // ViewModel handles all validation and authentication
        viewModel.login(username, password);
    }

    /**
     * Navigates to RegistrationActivity.
     */
    private void navigateToRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to MainActivity after successful login.
     *
     * @param userId The authenticated user's ID
     */
    private void startMainActivity(int userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
        finish();
    }
}