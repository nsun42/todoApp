package com.nsundareswaran.todoapp;

import java.util.Calendar;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private EditText etEditItem;
    private Button btnSaveItem;
    private DatePicker dpItemDate;
    private int RESULT_OK = 200;
    private int RESULT_FAIL = 401;
    public static String EDIT_ENTRY_TAG = "edit_entry";
    public static String EDIT_ENTRY_INDEX_TAG = "index";
    public static String EDIT_DATE_TAG = "date";
    private Task editTask = null;
    private int index = 0;
    private static final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etEditItem = (EditText) findViewById(R.id.etEditItem2);
        btnSaveItem = (Button) findViewById(R.id.btnSaveItem);
        dpItemDate = (DatePicker) findViewById(R.id.dpDate);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            System.out.println("Invalid input");
            returnResult(RESULT_FAIL);
        }
        editTask = new Task(bundle.getString(EDIT_ENTRY_TAG), bundle.getString(EDIT_DATE_TAG));
        index = bundle.getInt(EDIT_ENTRY_INDEX_TAG);

        initDatePicker();
        setEditEntry();
    }

    private void initDatePicker() {

        DatePicker.OnDateChangedListener datePickerListener= new DatePicker.OnDateChangedListener(){
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                System.out.println("Date is set to year:"+year+" month: "+month+" day: "+day);
                editTask.updateDate(year, month, day);
            }
        };

        dpItemDate.init(editTask.getYear(),(editTask.getMonth()), editTask.getDay(), datePickerListener);
    }
    private void returnResult(int resultCode) {
        Intent result = new Intent();
        Bundle bundle = null;
        if (resultCode != RESULT_FAIL) {
            bundle = new Bundle();
            bundle.putString(EDIT_ENTRY_TAG, editTask.name);
            bundle.putString(EDIT_DATE_TAG, editTask.date);
            bundle.putInt(EDIT_ENTRY_INDEX_TAG, index);
        }

        result.putExtras(bundle);
        setResult(resultCode, result);
        finish();
    }

    private void setEditEntry() {
        etEditItem.setText(editTask.name);
        etEditItem.setSelection(editTask.name.length());
    }

    public void onSave(View view) {
        editTask.name = etEditItem.getText().toString();
        returnResult(RESULT_OK);
    }
}
