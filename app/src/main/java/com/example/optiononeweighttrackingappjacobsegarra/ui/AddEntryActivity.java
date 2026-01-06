package com.example.optiononeweighttrackingappjacobsegarra.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.optiononeweighttrackingappjacobsegarra.R;
import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntry;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModel;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModelFactory;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for adding a new weight entry or editing an existing one.
 */
public class AddEntryActivity extends AppCompatActivity {

    public static final String EXTRA_ENTRY_JSON = "com.example.optiononeweighttrackingappjacobsegarra.EXTRA_ENTRY_JSON";
    public static final String EXTRA_USER_ID = "com.example.optiononeweighttrackingappjacobsegarra.EXTRA_USER_ID";

    private EntryViewModel entryViewModel;
    private EditText etWeight;
    private TextView tvDateDisplay;
    private TextView tvTitle;
    private Calendar selectedDate;
    private WeightEntry originalEntry = null;
    private int currentUserId = -1;

    // Constants for unit conversion (LBS to KG)
    private static final double KG_PER_LBS = 0.453592;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        // --- Get User ID First ---
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER_ID)) {
            currentUserId = intent.getIntExtra(EXTRA_USER_ID, -1);
        }

        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User ID is missing for AddEntryActivity.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // --- Initialize ViewModel ---
        EntryViewModelFactory factory = new EntryViewModelFactory(getApplication(), currentUserId);
        entryViewModel = new ViewModelProvider(this, factory).get(EntryViewModel.class);

        // Map UI components
        tvTitle = findViewById(R.id.tv_title);
        etWeight = findViewById(R.id.et_weight);
        tvDateDisplay = findViewById(R.id.tv_date_display);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);

        // Set up the window as a dialog
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Initialize selectedDate to today
        selectedDate = Calendar.getInstance();

        // Check if we are in EDIT mode
        if (intent.hasExtra(EXTRA_ENTRY_JSON)) {
            String entryJson = intent.getStringExtra(EXTRA_ENTRY_JSON);
            originalEntry = new Gson().fromJson(entryJson, WeightEntry.class);
            initializeForEditMode(originalEntry);
        } else {
            // Default display for ADD mode
            updateDateDisplay(selectedDate.getTime());
        }

        // Set up Date Picker Dialog
        tvDateDisplay.setOnClickListener(v -> showDatePickerDialog());

        btnSave.setOnClickListener(v -> saveEntry());
        btnCancel.setOnClickListener(v -> finish());
    }

    /** Pre-fills the form fields with data from an existing entry for editing. */
    private void initializeForEditMode(WeightEntry entry) {
        tvTitle.setText(R.string.title_edit_entry);

        // Convert KG (database unit) back to LBS (display unit) for the user
        double weightLbs = entry.getWeight() / KG_PER_LBS;

        etWeight.setText(String.format(Locale.getDefault(), "%.1f", weightLbs));

        selectedDate.setTimeInMillis(entry.getTimestamp());
        updateDateDisplay(selectedDate.getTime());
    }

    /** Displays the DatePickerDialog. */
    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    selectedDate.set(y, m, d);
                    updateDateDisplay(selectedDate.getTime());
                },
                year, month, day);

        // Prevent selecting a future date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /** Updates the TextView with the selected date in a readable format. */
    private void updateDateDisplay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        tvDateDisplay.setText(dateFormat.format(date));
    }

    /** Validates user input and saves/updates the entry via the ViewModel. */
    private void saveEntry() {
        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User ID is invalid.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String weightStr = etWeight.getText().toString().trim();

        if (TextUtils.isEmpty(weightStr)) {
            Toast.makeText(this, R.string.error_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        double weightLbs;
        try {
            weightLbs = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid weight format.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (weightLbs <= 0) {
            Toast.makeText(this, "Weight must be a positive value.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert LBS (user input) to KG (database unit)
        double weightKg = weightLbs * KG_PER_LBS;

        long timestamp = selectedDate.getTimeInMillis();
        WeightEntry finalEntry;

        if (originalEntry != null && originalEntry.getId() > 0) {
            // EDIT Mode
            finalEntry = new WeightEntry(originalEntry.getUserId(), weightKg, timestamp);
            finalEntry.setId(originalEntry.getId()); // Retain the original ID for update
            entryViewModel.updateWeightEntry(finalEntry); // <-- FIX HERE
            Toast.makeText(this, "Entry updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // ADD Mode
            finalEntry = new WeightEntry(currentUserId, weightKg, timestamp);
            entryViewModel.insertWeightEntry(finalEntry);
            Toast.makeText(this, R.string.toast_save_success, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
