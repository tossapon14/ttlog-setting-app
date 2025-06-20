package com.example.ttlogexample.lock.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.ttlogexample.R;
import com.example.ttlogexample.model.Building;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater layoutInflater;
    private final int icon;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> txt, int icon) {
        super(context, resource, txt);
        this.layoutInflater = LayoutInflater.from(context);
        this.icon = icon;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, true);
    }

    private View createView(int position, View convertView, ViewGroup parent, boolean isDropdown) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_spinner_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textContent = convertView.findViewById(R.id.text_content);
            viewHolder.iconSpinner = (ImageView) convertView.findViewById(R.id.icon_spinner);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String txt = getItem(position);

        if (txt != null) {
            viewHolder.textContent.setText(txt);
            if (position == 0) {
                viewHolder.textContent.setTextColor(Color.rgb(168,168,168));
            } else {
                viewHolder.iconSpinner.setImageResource(icon);
            }


            // Set an icon if available (assuming you have an icon resource or URL in your Building model)
            // For example:
            // viewHolder.iconSpinner.setImageResource(building.getIconResId());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textContent;
        ImageView iconSpinner;
    }
}