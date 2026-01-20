package com.example.fitnessapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.viewmodel.SettingsViewModel;
import com.example.fitnessapp.viewmodel.SettingsViewModelFactory;

/**
 * Allows users to update their goal weight and phone number.
 * Now uses SettingsViewModel for proper MVVM architecture
 */
public class SettingsActivity extends AppCompatActivity {

    // Intent extra key for passing user ID
    public static final String EXTRA_USER_ID = "USER_ID";

    private SettingsViewModel viewModel;
    private int userId;

    // UI Components (using actual IDs from activity_settings.xml)
    private EditText editTextGoalWeight;
    private EditText editTextPhoneNumber;
    private Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get user ID from intent
        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel with factory (needs userId)
        SettingsViewModelFactory factory = new SettingsViewModelFactory(getApplication(), userId);
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);

        initializeViews();
        setupObservers();
        setupListeners();
    }

    /**
     * Initialize view references
     */
    private void initializeViews() {
        editTextGoalWeight = findViewById(R.id.edit_goal_weight);
        editTextPhoneNumber = findViewById(R.id.edit_phone_number);
        btnSaveSettings = findViewById(R.id.btn_save_settings);
    }

    /**
     * Set up LiveData observers
     */
    private void setupObservers() {
        // Observe current user data
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                // Pre-fill fields with current values
                editTextGoalWeight.setText(String.valueOf(user.getGoalWeight()));
                editTextPhoneNumber.setText(user.getPhoneNumber());
            }
        });

        // Observe error messages
        viewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
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

        // Observe update success
        viewModel.updateSuccess.observe(this, success -> {
            if (success != null && success) {
                // Settings saved successfully
                viewModel.clearUpdateSuccess();
            }
        });

        // Observe loading state
        viewModel.isLoading.observe(this, isLoading -> {
            if (btnSaveSettings != null) {
                btnSaveSettings.setEnabled(!isLoading);
            }
        });
    }

    /**
     * Set up button listeners
     */
    private void setupListeners() {
        if (btnSaveSettings != null) {
            btnSaveSettings.setOnClickListener(v -> saveSettings());
        }
    }

    /**
     * Save settings when button is clicked
     */
    private void saveSettings() {
        String goalWeightStr = editTextGoalWeight.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        // Validate goal weight
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

        // Update through ViewModel
        viewModel.updateGoalWeight(goalWeight);
        viewModel.updatePhoneNumber(phoneNumber);
    }
}