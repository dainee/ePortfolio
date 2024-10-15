package com.example.weighttrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class WeightLogAdapter extends BaseAdapter {

    private Context context;
    private List<WeightLogEntry> weightLogEntries;
    private UserDataManager userDataManager;

    public WeightLogAdapter(Context context, List<WeightLogEntry> weightLogEntries, UserDataManager userDataManager) {
        this.context = context;
        this.weightLogEntries = weightLogEntries;
        this.userDataManager = userDataManager;
    }

    @Override
    public int getCount() {
        return weightLogEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return weightLogEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_weight_log, parent, false);
        }

        WeightLogEntry entry = weightLogEntries.get(position);

        TextView weightTextView = convertView.findViewById(R.id.weight);
        TextView dateTextView = convertView.findViewById(R.id.date);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        // Set text for weight and date
        weightTextView.setText(String.format("%.1f lbs", entry.getWeight()));
        dateTextView.setText(entry.getDate());

        // Set click listener for delete button
        deleteButton.setOnClickListener(v -> {
            WeightLogEntry entryToDelete = weightLogEntries.get(position);
            userDataManager.deleteWeightEntry(entryToDelete.getEntryId());  // Delete from the database
            weightLogEntries.remove(position);  // Remove from the list
            notifyDataSetChanged();  // Update the list view
        });

        return convertView;
    }
}
