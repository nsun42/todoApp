package com.nsundareswaran.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by nsundareswaran on 7/5/16.
 */
public class CustomCursorAdapter extends CursorAdapter {

    public CustomCursorAdapter(Context c, Cursor cursor) {
        super(c, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String task = cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_ITEM_NAME));
        TextView textView = (TextView) view.findViewById(R.id.tvItemName);
        textView.setText(task);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        String task = cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_ITEM_NAME));

        TextView textView = (TextView) view.findViewById(R.id.tvItemName);
        textView.setText(task);
        return view;
    }
}
