package com.nsundareswaran.todoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private CustomCursorAdapter tasksAdapter;
    private ListView tasksListView;
    private EditText taskToAdd;
    private int EDIT_REQUEST_CODE = 1;
    private int RESULT_OK = 200;
    private String LOG_TAG = "MainActivity";
    private static ToDoListDatabaseHelper toDoListDatabaseHelper;
    private Cursor cursor = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasksListView = (ListView) findViewById(R.id.lvItems);
        taskToAdd = (EditText) findViewById(R.id.etEditItem);
        initializeTaskList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    private void initializeTaskList() {
        toDoListDatabaseHelper = ToDoListDatabaseHelper.getInstance(this);
        if (toDoListDatabaseHelper == null) {
            return;
        }
        cursor = toDoListDatabaseHelper.getCursor();
        tasksAdapter = new CustomCursorAdapter(this, cursor);
        tasksListView.setAdapter(tasksAdapter);

        // Long click on a task to Delete
        tasksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                cursor.moveToPosition(index);
                int id = cursor.getInt(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_ID));
                toDoListDatabaseHelper.deleteEntry(id);
                updateAdapter();
                return true;
            }
        });

        // Click a task to Edit
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                String editEntry =  cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_ITEM_NAME));
                String editDate = cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_DATE));
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(EditActivity.EDIT_ENTRY_TAG, editEntry);
                bundle.putString(EditActivity.EDIT_DATE_TAG, editDate);
                bundle.putInt(EditActivity.EDIT_ENTRY_INDEX_TAG, i);
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    public void onAdd(View view) {
        String taskName = taskToAdd.getText().toString();
        Task newTask = new Task(taskName);
        taskToAdd.setText("");
        toDoListDatabaseHelper.addDbEntry(newTask);
        updateAdapter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Task newTask = new Task(
                    bundle.getString(EditActivity.EDIT_ENTRY_TAG),
                    bundle.getString(EditActivity.EDIT_DATE_TAG)
            );
            int index = bundle.getInt(EditActivity.EDIT_ENTRY_INDEX_TAG);
            cursor.moveToPosition(index);
            Task oldTask = new Task(
                    cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_ITEM_NAME)),
                    cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_DATE))
            );
            toDoListDatabaseHelper.updateEntry(oldTask, newTask);
            updateAdapter();
        }
    }

    private void updateAdapter() {
        Cursor newCursor = toDoListDatabaseHelper.getCursor();
        tasksAdapter.changeCursor(newCursor);
        cursor = newCursor;
        tasksAdapter.notifyDataSetChanged();
    }
}
