package com.example.optiononeweighttrackingappjacobsegarra;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntry;
import com.example.optiononeweighttrackingappjacobsegarra.databinding.ActivityDashboardBinding;
import com.example.optiononeweighttrackingappjacobsegarra.ui.WeightAdapter;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModel;
import com.example.optiononeweighttrackingappjacobsegarra.viewmodel.EntryViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

/**
 * Dashboard Activity - Displays all weight entries in a GRID format
 * Implements full CRUD operations:
 * - CREATE: Add new entries via FAB
 * - READ: Display all entries in grid
 * - UPDATE: Edit entries by clicking on them
 * - DELETE: Delete entries via delete button
 */
public class DashboardActivity extends AppCompatActivity implements WeightAdapter.OnItemActionListener {

    public static final String EXTRA_USER_ID = "com.example.optiononeweighttrackingappjacobsegarra.USER_ID";

    private ActivityDashboardBinding binding;
    private EntryViewModel viewModel;
    private WeightAdapter adapter;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get user ID from intent
        currentUserId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weight History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize ViewModel
        EntryViewModelFactory factory = new EntryViewModelFactory(getApplication(), currentUserId);
        viewModel = new ViewModelProvider(this, factory).get(EntryViewModel.class);

        // Setup RecyclerView with Grid Layout (2 columns)
        setupRecyclerView();

        // Setup FAB for adding new entries
        setupFab();

        // Observe data changes
        observeData();
    }

    private void setupRecyclerView() {
        // Initialize adapter with this activity as listener
        adapter = new WeightAdapter(this);

        // Use GridLayoutManager for grid display (2 columns)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerViewEntries.setLayoutManager(gridLayoutManager);
        binding.recyclerViewEntries.setAdapter(adapter);
        binding.recyclerViewEntries.setHasFixedSize(true);
    }

    private void setupFab() {
        binding.fabAddEntry.setOnClickListener(v -> {
            // Navigate to AddEntryActivity for CREATE operation
            Intent intent = new Intent(this, com.example.optiononeweighttrackingappjacobsegarra.ui.AddEntryActivity.class);
            intent.putExtra(com.example.optiononeweighttrackingappjacobsegarra.ui.AddEntryActivity.EXTRA_USER_ID, currentUserId);
            startActivity(intent);
        });
    }

    private void observeData() {
        // Observe weight entries from database - READ operation
        viewModel.getAllEntriesForUser().observe(this, entries -> {
            if (entries != null && !entries.isEmpty()) {
                adapter.setWeightEntries(entries);
                binding.textEmptyState.setVisibility(android.view.View.GONE);
                binding.recyclerViewEntries.setVisibility(android.view.View.VISIBLE);
            } else {
                // Show empty state
                binding.textEmptyState.setVisibility(android.view.View.VISIBLE);
                binding.recyclerViewEntries.setVisibility(android.view.View.GONE);
            }
        });
    }

    // DELETE operation - called when user clicks delete button
    @Override
    public void onDeleteClick(WeightEntry entry) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this weight entry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteWeightEntry(entry);
                    Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // UPDATE operation - called when user clicks edit button
    public void onEditClick(WeightEntry entry) {
        Intent intent = new Intent(this, com.example.optiononeweighttrackingappjacobsegarra.ui.AddEntryActivity.class);
        intent.putExtra(com.example.optiononeweighttrackingappjacobsegarra.ui.AddEntryActivity.EXTRA_USER_ID, currentUserId);
        intent.putExtra(com.example.optiononeweighttrackingappjacobsegarra.ui.AddEntryActivity.EXTRA_ENTRY_JSON,
                new Gson().toJson(entry));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_USER_ID, currentUserId);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}