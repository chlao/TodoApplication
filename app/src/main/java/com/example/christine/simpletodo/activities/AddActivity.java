package com.example.christine.simpletodo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.christine.simpletodo.R;
import com.example.christine.simpletodo.utils.ToDoItemDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    EditText taskName;
    Spinner priority;
    DatePicker dueDate;

    Bundle extras = new Bundle();
    ToDoItemDatabase db = ToDoItemDatabase.getInstance(this);

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
        if( taskName.getText().toString().trim().equals(""))
        {
            Context context = getApplicationContext();
            CharSequence text = "Please enter a task name!";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

        } else if (db.checkDuplicate(taskName.getText().toString())) {
            Context context = getApplicationContext();
            CharSequence text = "A task with that name already exists!";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

            setResult(Activity.RESULT_CANCELED);
        }
        else {
            Intent resultIntent = new Intent();

            extras.putString("itemName", taskName.getText().toString());
            extras.putString("priority", priority.getSelectedItem().toString());

            String dueDateStr = (dueDate.getMonth() + 1) + "/" + dueDate.getDayOfMonth() + "/" + dueDate.getYear();

            extras.putString("dueDate", dueDateStr);

            resultIntent.putExtras(extras);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
