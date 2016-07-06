package com.nsundareswaran.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by nsundareswaran on 7/5/16.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {
    public CustomArrayAdapter(Context context, ArrayList<String> list) {
        super(context, R.layout.list_item, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.tvItemName);
        textView.setText(item);

        return convertView;
    }
}
