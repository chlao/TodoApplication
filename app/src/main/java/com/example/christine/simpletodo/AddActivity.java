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

public class AddActivity extends AppCompatActivity {
    EditText taskName;
    Spinner priority;
    DatePicker dueDate;
    Bundle extras = new Bundle();
    String dueDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setTitle("Add a new task");

        taskName = (EditText) findViewById(R.id.addTaskName);
        priority = (Spinner) findViewById(R.id.addPriority);
        dueDate = (DatePicker) findViewById(R.id.addDueDate);

        List<String> categories = new ArrayList<String>();
        categories.add("HIGH");
        categories.add("MEDIUM");
        categories.add("LOW");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        priority.setAdapter(adapter);
    }

    public void onAddItem(View view) {
        Intent resultIntent = new Intent();

        extras.putString("itemName", taskName.getText().toString());
        extras.putString("priority", priority.getSelectedItem().toString());

        String dueDateStr = dueDate.getMonth() + "/" + dueDate.getDayOfMonth() + "/" + dueDate.getYear();

        extras.putString("dueDate", dueDateStr);

        resultIntent.putExtras(extras);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
