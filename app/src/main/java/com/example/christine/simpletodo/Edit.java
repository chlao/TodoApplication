package com.example.christine.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Edit extends AppCompatActivity {
    EditText editText;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        extras = getIntent().getExtras();

        editText = (EditText) findViewById(R.id.editText);

        editText.setText(extras.getString("prevText"));

        // Move cursor to the end
        editText.setSelection(editText.getText().length());
        // Set Focus to text field
        editText.requestFocus();
    }

    public void saveEdit(View view){
        Intent resultIntent = new Intent();

        extras.putString("newItemName", editText.getText().toString());

        resultIntent.putExtras(extras);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
