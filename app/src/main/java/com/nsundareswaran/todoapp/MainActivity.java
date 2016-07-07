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

public class MainActivity extends AppCompatActivity {
    //private ArrayList<String> tasks;
    //private ArrayAdapter<String> tasksAdapter;
    private CustomCursorAdapter tasksAdapter;
    private ListView tasksListView;
    private EditText taskToAdd;
    private int EDIT_REQUEST_CODE = 1;
    private int RESULT_OK = 200;
    private String LOG_TAG = "MainActivity";
    private String EDIT_ENTRY_TAG = "edit_entry";
    private String EDIT_ENTRY_INDEX_TAG = "index";
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
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EDIT_ENTRY_TAG, editEntry);
                intent.putExtra(EDIT_ENTRY_INDEX_TAG,i);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    public void onAdd(View view) {
        String newTask = taskToAdd.getText().toString();
        taskToAdd.setText("");
        toDoListDatabaseHelper.addDbEntry(newTask);
        updateAdapter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String edittedItem = data.getExtras().getString(EDIT_ENTRY_TAG);
            int index = data.getExtras().getInt(EDIT_ENTRY_INDEX_TAG);
            System.out.println("edittedItem "+ edittedItem+" index "+ index);
            cursor.moveToPosition(index);
            String oldEntry = cursor.getString(cursor.getColumnIndex(ToDoListDatabaseHelper.COLUMN_ITEM_NAME));
            toDoListDatabaseHelper.updateEntry(oldEntry, edittedItem);
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
