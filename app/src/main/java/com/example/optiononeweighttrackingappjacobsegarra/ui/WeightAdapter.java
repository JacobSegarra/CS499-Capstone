package com.example.optiononeweighttrackingappjacobsegarra.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optiononeweighttrackingappjacobsegarra.AppConstants;
import com.example.optiononeweighttrackingappjacobsegarra.R;
import com.example.optiononeweighttrackingappjacobsegarra.data.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying weight entries in a list/grid format (READ operation).
 * This class keeps the UI logic separate and concise.
 */
public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    private List<WeightEntry> weightList = new ArrayList<>();
    private final OnItemActionListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    /** Interface to handle interactions from the grid items (Delete only). */
    public interface OnItemActionListener {
        void onDeleteClick(WeightEntry entry);
    }

    public WeightAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the adapter's data set and notifies the RecyclerView.
     * @param newWeightList The new list of WeightEntry objects.
     */
    public void setWeightEntries(List<WeightEntry> newWeightList) {
        this.weightList = newWeightList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_weight, parent, false);
        return new WeightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        WeightEntry currentEntry = weightList.get(position);
        holder.bind(currentEntry);

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(currentEntry));
    }

    @Override
    public int getItemCount() {
        return weightList.size();
    }

    /** ViewHolder class for a single item in the list. */
    public class WeightViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvWeight;
        public final ImageButton btnDelete;

        public WeightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.text_date);
            tvWeight = itemView.findViewById(R.id.text_weight);
            btnDelete = itemView.findViewById(R.id.button_delete);
        }

        public void bind(WeightEntry entry) {
            // Display date from timestamp
            tvDate.setText(dateFormat.format(new java.util.Date(entry.getTimestamp())));

            // Convert KG (Database) to LBS (UI Display)
            double weightLbs = entry.getWeight() * AppConstants.LBS_PER_KG;
            tvWeight.setText(String.format(Locale.getDefault(), "%.1f lbs", weightLbs));
        }
    }
}