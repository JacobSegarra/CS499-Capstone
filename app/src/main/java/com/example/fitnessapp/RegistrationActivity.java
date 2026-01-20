package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.viewmodel.RegistrationViewModel;

/**
 * Handles user registration using RegistrationViewModel
 * Now uses proper MVVM architecture with ViewModel and LiveData
 */
public class RegistrationActivity extends AppCompatActivity {

    private RegistrationViewModel viewModel;

    // UI Components (using actual IDs from activity_registration.xml)
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextGoalWeight;
    private EditText editTextPhoneNumber;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        // Find views with CORRECT IDs
        initializeViews();

        setupObservers();
        setupListeners();
    }

    /**
     * Initializes all view references with correct IDs from XML
     */
    private void initializeViews() {
        editTextUsername = findViewById(R.id.edit_text_username_reg);
        editTextPassword = findViewById(R.id.edit_text_password_reg);
        editTextGoalWeight = findViewById(R.id.edit_text_goal_weight_reg);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number_reg);
        btnRegister = findViewById(R.id.btn_complete_registration);
    }

    /**
     * Sets up LiveData observers for ViewModel state changes
     */
    private void setupObservers() {
        // Observe registration result
        viewModel.registrationResult.observe(this, userId -> {
            if (userId != null && userId > 0) {
                // Registration successful
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
                navigateToLogin();
                viewModel.clearRegistrationResult();
            }
        });

        // Observe error messages
        viewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                viewModel.clearError();
            }
        });

        // Observe success messages
        viewModel.successMessage.observe(this, successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                viewModel.clearSuccess();
            }
        });

        // Observe loading state
        viewModel.isLoading.observe(this, isLoading -> {
            if (btnRegister != null) {
                btnRegister.setEnabled(!isLoading);
            }
        });
    }

    /**
     * Sets up button click listeners
     */
    private void setupListeners() {
        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> handleRegistration());
        }
    }

    /**
     * Handles registration button click
     */
    private void handleRegistration() {
        // Get input values
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = password; // Note: Your layout doesn't have confirm password field
        String goalWeightStr = editTextGoalWeight.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        // Validate goal weight input
        if (goalWeightStr.isEmpty()) {
            Toast.makeText(this, "Please enter a goal weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double goalWeight;
        try {
            goalWeight = Double.parseDouble(goalWeightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid goal weight format", Toast.LENGTH_SHORT).show();
            return;
        }

        // ViewModel handles all validation, hashing, and registration
        viewModel.registerUser(username, password, confirmPassword, goalWeight, phoneNumber);
    }

    /**
     * Navigates back to LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}