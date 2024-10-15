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

    public WeightLogAdapter(Context context, List<WeightLogEntry> weightLogEntries) {
        this.context = context;
        this.weightLogEntries = weightLogEntries;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_weight_log, parent, false);
        }

        WeightLogEntry entry = weightLogEntries.get(position);

        TextView weightTextView = convertView.findViewById(R.id.weight);
        TextView dateTextView = convertView.findViewById(R.id.date);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        weightTextView.setText(String.format("%.1f lbs", entry.getWeight()));
        dateTextView.setText(entry.getDate());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightLogEntries.remove(position);
                notifyDataSetChanged();
                // Optionally, save changes to a database or SharedPreferences
            }
        });

        return convertView;
    }
}
