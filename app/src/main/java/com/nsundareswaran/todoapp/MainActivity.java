package com.nsundareswaran.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> tasks;
    //private ArrayAdapter<String> tasksAdapter;
    private CustomArrayAdapter tasksAdapter;
    private ListView tasksListView;
    private EditText taskToAdd;
    private int EDIT_REQUEST_CODE = 1;
    private int RESULT_OK = 200;
    private String LOG_TAG = "MainActivity";
    private String EDIT_ENTRY_TAG = "edit_entry";
    private String EDIT_ENTRY_INDEX_TAG = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasksListView = (ListView) findViewById(R.id.lvItems);
        taskToAdd = (EditText) findViewById(R.id.etEditItem);
        initializeTaskList();
    }

    private void initializeTaskList() {
        tasks = new ArrayList<String>();
        readTasks();
        //tasksAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  tasks);
        tasksAdapter = new CustomArrayAdapter(this, tasks);
        tasksListView.setAdapter(tasksAdapter);

        // Long click on a task to Delete
        tasksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                tasks.remove(index);
                tasksAdapter.notifyDataSetChanged();
                writeTasks();
                return true;
            }
        });

        // Click a task to Edit
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String editEntry =  tasks.get(i);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EDIT_ENTRY_TAG, editEntry);
                intent.putExtra(EDIT_ENTRY_INDEX_TAG,i);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    private void readTasks() {
        File fileDir = getFilesDir();
        File persistFile = new File(fileDir, "todoList.txt");
        try {
             tasks = new ArrayList<String>(FileUtils.readLines(persistFile));
        } catch (java.io.IOException ex) {
            Log.e(LOG_TAG, " Exception "+ex+" read tasks failed");
        }
    }

    private void writeTasks() {
        File fileDir = getFilesDir();
        File persistFile = new File(fileDir, "todoList.txt");
        try {
            FileUtils.writeLines(persistFile,  tasks);
        } catch (java.io.IOException ex) {
            Log.e(LOG_TAG, " Exception "+ex+" persist tasks failed");
        }
    }

    public void onAdd(View view) {
        String newTask = taskToAdd.getText().toString();
        taskToAdd.setText("");
         tasks.add(newTask);
        writeTasks();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String edittedItem = data.getExtras().getString(EDIT_ENTRY_TAG);
            int index = data.getExtras().getInt(EDIT_ENTRY_INDEX_TAG);
            System.out.println("edittedItem "+ edittedItem+" index "+ index);
             tasks.set(index, edittedItem);
            tasksAdapter.notifyDataSetChanged();
            writeTasks();
        }
    }
}
