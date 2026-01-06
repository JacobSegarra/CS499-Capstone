package com.example.optiononeweighttrackingappjacobsegarra.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optiononeweighttrackingappjacobsegarra.R;
// FIX 1: Import the correct Entity class
import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying a list of weight entries.
 * It uses the IDs provided in the user's list_item_weight.xml layout.
 */
@SuppressWarnings("deprecation") // Suppressing warnings related to RecyclerView methods
public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.EntryViewHolder> {

    // SimpleDateFormat for displaying date and time in a readable format
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy @ h:mm a", Locale.getDefault());
    private List<WeightEntry> entries = new ArrayList<>();
    private OnItemClickListener listener;

    /**
     * Interface to handle item clicks, specifically for the delete button.
     */
    public interface OnItemClickListener {
        void onDeleteClick(WeightEntry entry);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_weight, parent, false);
        return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        WeightEntry currentEntry = entries.get(position);

        // 1. Format and set the date/time
        String formattedDate = dateFormat.format(currentEntry.getTimestamp());
        holder.textViewDate.setText(formattedDate);

        // 2. Set the weight (formatted to one decimal place)
        String weightText = String.format(Locale.getDefault(), "%.1f kg", currentEntry.getWeight());
        holder.textViewWeight.setText(weightText);

        // 3. Set the note (or a placeholder if null or empty)
        holder.textViewNote.setText("—");

        // 4. Set click listener for the delete button
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                // Pass the entry object to the listener for deletion
                listener.onDeleteClick(currentEntry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    /**
     * Public method to update the adapter's data set.
     * This is called by the LiveData observer in DashboardActivity.
     * @param newEntries The new list of entries.
     */
    public void setEntries(List<WeightEntry> newEntries) {
        // List<Entry> reversedList = new ArrayList<>(newEntries);
        this.entries = newEntries; // Use the list as returned by LiveData/DAO
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class to hold references to the views for each list item.
     */
    static class EntryViewHolder extends RecyclerView.ViewHolder {
        // Updated TextView IDs to match the user's XML (text_date, text_weight, text_notes)
        private final TextView textViewDate;
        private final TextView textViewWeight;
        private final TextView textViewNote;
        // Updated ImageButton ID to match the user's XML (button_delete)
        private final ImageButton buttonDelete;

        public EntryViewHolder(View itemView) {
            super(itemView);
            // Mapping views using the user's specified IDs
            textViewDate = itemView.findViewById(R.id.text_date);
            textViewWeight = itemView.findViewById(R.id.text_weight);
            textViewNote = itemView.findViewById(R.id.text_notes);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}