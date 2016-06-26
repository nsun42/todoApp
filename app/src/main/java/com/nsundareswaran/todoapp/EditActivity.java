package com.nsundareswaran.todoapp;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private EditText etEditItem;
    private Button btnSaveItem;
    private int RESULT_OK = 200;
    private int RESULT_FAIL = 401;
    private int index;
    private String EDIT_ENTRY_TAG = "edit_entry";
    private String EDIT_ENTRY_INDEX_TAG = "index";
    private String editEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etEditItem = (EditText) findViewById(R.id.etEditItem2);
        btnSaveItem = (Button) findViewById(R.id.btnSaveItem);
        editEntry= getIntent().getStringExtra(EDIT_ENTRY_TAG);
        index = getIntent().getIntExtra(EDIT_ENTRY_INDEX_TAG, -1);
        if (editEntry == null || index == -1) {
            returnResult(RESULT_FAIL, "", -1);
        }
        setEditEntry();
    }

    private void returnResult(int resultCode, String entry, int indx) {
        Intent result = new Intent();
        result.putExtra(EDIT_ENTRY_TAG, entry);
        result.putExtra(EDIT_ENTRY_INDEX_TAG, indx);
        setResult(resultCode, result);
        finish();
    }

    private void setEditEntry() {
        etEditItem.setText(editEntry);
        etEditItem.setSelection(editEntry.length());
    }

    public void onSave(View view) {
        String editedItem = etEditItem.getText().toString();
        returnResult(RESULT_OK, editedItem, index);
    }
}
