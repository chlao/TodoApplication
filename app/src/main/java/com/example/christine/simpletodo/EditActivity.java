package com.example.christine.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    EditText taskName;
    Spinner priority;
    DatePicker dueDate;

    private static final int HIGH_PRIORITY_POS = 0;
    private static final int MEDIUM_PRIORITY_POS = 1;
    private static final int LOW_PRIORITY_POS = 2;

    private static final String HIGH_PRIORITY_STR = "HIGH";
    private static final String MEDIUM_PRIORITY_STR = "MEDIUM";
    private static final String LOW_PRIORITY_STR = "LOW";

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        int prioritySelection;

        setTitle("Edit your task");

        extras = getIntent().getExtras();

        taskName = (EditText) findViewById(R.id.editTaskName);
        priority = (Spinner) findViewById(R.id.editPriority);
        dueDate = (DatePicker) findViewById(R.id.editDueDate);

        List<String> categories = new ArrayList<String>();
        categories.add("HIGH");
        categories.add("MEDIUM");
        categories.add("LOW");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        priority.setAdapter(adapter);

        taskName.setText(extras.getString("prevName"));

        // Move cursor to the end
        taskName.setSelection(taskName.getText().length());
        // Set Focus to text field
        taskName.requestFocus();

        prioritySelection = convertPriorityToInt(extras.getString("prevPriority"));

        if (prioritySelection != -1) {
            priority.setSelection(prioritySelection);
        }
    }

    public void saveEdit(View view){
        String prioritySelection;
        Intent resultIntent = new Intent();

        extras.putString("newItemName", taskName.getText().toString());
        extras.putString("newDueDate", dueDate.getMonth() + "/" + dueDate.getDayOfMonth() + "/" + dueDate.getYear());

        prioritySelection = convertPriorityToString(priority.getSelectedItemPosition());

        extras.putString("newPriority", prioritySelection);

        resultIntent.putExtras(extras);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public String convertPriorityToString(int priority) {
        switch (priority) {
            case HIGH_PRIORITY_POS:
                return HIGH_PRIORITY_STR;
            case MEDIUM_PRIORITY_POS:
                return MEDIUM_PRIORITY_STR;
            case LOW_PRIORITY_POS:
                return LOW_PRIORITY_STR;
            default:
                return "";
        }
    }

    public int convertPriorityToInt(String priority){
        switch (priority){
            case HIGH_PRIORITY_STR:
                return HIGH_PRIORITY_POS;
            case MEDIUM_PRIORITY_STR:
                return MEDIUM_PRIORITY_POS;
            case LOW_PRIORITY_STR:
                return LOW_PRIORITY_POS;
            default:
                return -1;
        }
    }

}
