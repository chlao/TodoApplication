package com.example.christine.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOExceptionWithCause;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> atodoAdapter;
    ListView lvItems;
    EditText etEditText;

    public static final int EDIT_REQUEST = 5;

    @Override
    // XML layout for the activity is applied in onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(atodoAdapter);

        etEditText = (EditText) findViewById(R.id.etEditText);

        setupListViewListener();
    }

    public void populateArrayItems(){
        todoItems = new ArrayList<String>();
        /*todoItems.add("Item1");
        todoItems.add("Item2");
        todoItems.add("Item3");*/
        readItems();

        // Array adapter allows us to easily display the contents of an ArrayList w/in a ListView
        /** Paramters
         *  1. context - provides information (e.g. styles to be applied to views - instance of todo)
         *  2. resource file for each row (TextView)
         *  3. Arraylist
         */
        atodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems(){
        // Reference to special directory that this application is allowed to read from
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e){
        }
    }

    private void writeItems(){
        // Reference to special directory that this application is allowed to read from
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e){

        }
    }

    // View that's passed in is the button
    public void onAddItem(View view) {
        atodoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                todoItems.remove(pos);
                // If modify underlying data structure, need to notify adapter (else exception thrown and app crashes)
                atodoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                Intent intent = new Intent(getBaseContext(), Edit.class);
                Bundle extras = new Bundle();
                extras.putString("prevText", todoItems.get(pos).toString());
                extras.putInt("pos", pos);
                intent.putExtras(extras);
                //intent.putExtra("prevText", todoItems.get(pos).toString());

                startActivityForResult(intent, EDIT_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras;
        int pos;
        String newText;

        //super.onActivityResult(requestCode, resultCode, data);
        // Check which response we're responding to
        if (requestCode == EDIT_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                extras = data.getExtras();
                pos = extras.getInt("pos");
                newText = extras.getString("newItemName");

                todoItems.set(pos, newText);

                atodoAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }
}
