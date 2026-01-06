package com.example.optiononeweighttrackingappjacobsegarra;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.optiononeweighttrackingappjacobsegarra.data.User;
import com.example.optiononeweighttrackingappjacobsegarra.databinding.ActivitySettingsBinding;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModel;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModelFactory;

/**
 * Activity for managing user settings, goal weight and SMS phone number.
 * It uses the EntryViewModel to observe and update the User entity.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "com.example.optiononeweighttrackingappjacobsegarra.USER_ID";

    private ActivitySettingsBinding binding;
    private EntryViewModel viewModel;
    private int currentUserId;
    private User currentUser; // Hold a local copy of the User object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Get User ID from Intent
        currentUserId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 2. Setup Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("User Settings");
        }

        // 3. Initialize ViewModel using the Factory
        EntryViewModelFactory factory = new EntryViewModelFactory(getApplication(), currentUserId);
        viewModel = new ViewModelProvider(this, factory).get(EntryViewModel.class);

        // 4. Observe the current User object to pre-fill fields
        observeUser();

        // 5. Setup Save Listener
        binding.btnSaveSettings.setOnClickListener(v -> handleSaveSettings());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Observes the LiveData for the current user and populates the UI fields
     * when the data is loaded from the database.
     */
    private void observeUser() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                currentUser = user; // Store the loaded user object locally
                binding.editGoalWeight.setText(String.valueOf(user.getGoalWeight()));
                binding.editPhoneNumber.setText(user.getPhoneNumber());
            } else {
                Toast.makeText(this, "User data could not be loaded.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles validation and updates the user's settings in the database.
     */
    private void handleSaveSettings() {
        if (currentUser == null) {
            Toast.makeText(this, "Wait for user data to load before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        String goalText = binding.editGoalWeight.getText().toString().trim();
        String phone = binding.editPhoneNumber.getText().toString().trim();

        if (goalText.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Both goal weight and phone number are required.", Toast.LENGTH_LONG).show();
            return;
        }

        // Basic Phone Validation
        if (phone.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number (min 10 digits).", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            double newGoalWeight = Double.parseDouble(goalText);

            // 1. Create an updated User object
            User updatedUser = new User(
                    currentUser.getUsername(),
                    currentUser.getPassword(),
                    newGoalWeight,
                    phone
            );
            updatedUser.setId(currentUser.getId());

            // 2. Call ViewModel to update the database
            viewModel.updateUser(updatedUser);

            Toast.makeText(this, "Settings updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to MainActivity

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Goal Weight format. Must be a number.", Toast.LENGTH_SHORT).show();
        }
    }
}