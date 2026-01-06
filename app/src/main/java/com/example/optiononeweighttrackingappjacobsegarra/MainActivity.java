package com.example.optiononeweighttrackingappjacobsegarra;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.optiononeweighttrackingappjacobsegarra.data.User;
import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntry;
import com.example.optiononeweighttrackingappjacobsegarra.databinding.ActivityMainBinding;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModel;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModelFactory;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The main screen after login/registration. Handles weight logging,
 * displays historical data, checks goals, and requests SMS permissions.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "com.example.optiononeweighttrackingappjacobsegarra.USER_ID";
    private static final int SMS_PERMISSION_REQUEST_CODE = 1001;

    private ActivityMainBinding binding;
    private EntryViewModel viewModel;
    private int currentUserId;
    private User currentUser;

    // Hardcoded for testing purposes as height is not collected in RegistrationActivity
    private static final double DEFAULT_HEIGHT_METERS = 1.75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Get User ID
        currentUserId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (currentUserId == -1) {
            // Should not happen if login/reg is successful
            navigateToLogin();
            return;
        }

        // 2. Initialize ViewModel
        EntryViewModelFactory factory = new EntryViewModelFactory(getApplication(), currentUserId);
        viewModel = new ViewModelProvider(this, factory).get(EntryViewModel.class);

        // 3. Setup UI and Listeners
        setupToolbar();
        observeData();
        binding.btnLogWeight.setOnClickListener(v -> handleWeightLog());

        // 4. Request SMS Permission on start
        requestSmsPermission();
    }


    private void observeData() {
        // Observe User data
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                currentUser = user;
                binding.textGoalWeight.setText(String.format(Locale.getDefault(), "Goal: %.1f kg", user.getGoalWeight()));
            } else {
                binding.textGoalWeight.setText(R.string.text_error_goal);
            }
        });

        // Observe Weight Entries data (for history and latest weight)
        viewModel.getAllEntriesForUser().observe(this, entries -> {
            if (entries != null && !entries.isEmpty()) {
                updateUIWithData(entries);
            } else {
                // Initial state: no entries
                binding.textLatestWeight.setText(R.string.text_no_entries);
                binding.textBmiStatus.setText("");
                binding.textGoalProgress.setText("");
            }
        });
    }

    // --- UI Update and Calculations ---

    private void updateUIWithData(List<WeightEntry> entries) {
        if (currentUser == null) return; // Wait until user data is loaded

        WeightEntry latestEntry = entries.get(0); // Entries are ordered by timestamp
        double latestWeight = latestEntry.getWeight();

        // 1. Update Latest Weight
        binding.textLatestWeight.setText(String.format(Locale.getDefault(), "Latest: %.1f kg", latestWeight));

        // 2. Calculate and Update BMI
        // BMI = Weight (kg) / (Height (m) * Height (m))
        double bmi = latestWeight / (DEFAULT_HEIGHT_METERS * DEFAULT_HEIGHT_METERS);
        String bmiStatus = getBmiStatus(bmi);
        binding.textBmiStatus.setText(String.format(Locale.getDefault(), "BMI: %.2f (%s)", bmi, bmiStatus));

        // 3. Update Goal Progress
        double goalWeight = currentUser.getGoalWeight();
        double remaining = latestWeight - goalWeight;
        if (remaining <= 0) {
            binding.textGoalProgress.setText(R.string.text_goal_reached);
        } else {
            binding.textGoalProgress.setText(String.format(Locale.getDefault(), "Remaining: %.1f kg", remaining));
        }

        // 4. Placeholder for Chart
        displayChartPlaceholder(entries);
    }

    private String getBmiStatus(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    private void displayChartPlaceholder(List<WeightEntry> entries) {
        binding.textChartPlaceholder.setText(String.format(Locale.getDefault(),
                "Chart placeholder active. Total entries: %d", entries.size()));
        binding.textChartPlaceholder.setVisibility(View.VISIBLE);
    }

    // --- Weight Logging ---

    private void handleWeightLog() {
        String weightText = binding.editTextNewWeight.getText().toString().trim();

        if (weightText.isEmpty()) {
            Toast.makeText(this, "Please enter a weight value.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double newWeight = Double.parseDouble(weightText);

            if (newWeight <= 0) {
                Toast.makeText(this, "Weight must be greater than zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new WeightEntry object
            WeightEntry newEntry = new WeightEntry(
                    currentUserId,
                    newWeight,
                    new Date().getTime()
            );

            viewModel.insertWeightEntry(newEntry);

            binding.editTextNewWeight.setText("");
            Toast.makeText(this, String.format(Locale.getDefault(), "Logged %.1f kg", newWeight), Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid weight format. Must be a number.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(DashboardActivity.EXTRA_USER_ID, currentUserId);
        startActivity(intent);
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Ensure you have main_menu.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle Settings Navigation
        if (id == R.id.action_settings) {
            navigateToSettings();
            return true;
        }

        // Handle Logout
        else if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        else if (id == R.id.action_view_history) {
            navigateToDashboard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.EXTRA_USER_ID, currentUserId);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Clear activity stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void logoutUser() {
        Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    // --- Permissions Handling for SMS ---

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission granted. Goal alerts are active.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS Permission denied. Cannot send goal alerts.", Toast.LENGTH_LONG).show();
            }
        }
    }
}