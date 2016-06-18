package com.example.christine.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    //ArrayList<String> todoItems;
    ArrayList<Item> todoItems;
    ItemAdapter atodoAdapter;
    //ArrayAdapter<String> atodoAdapter;
    ListView lvItems;
    EditText etEditText;
    ToDoItemDatabase db;

    public static final int EDIT_REQUEST = 5;
    public static final int ADD_REQUEST = 6;

    private static final String TAG = "MainActivity";

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
        //todoItems = new ArrayList<String>();
        todoItems = new ArrayList<Item>();

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
        //atodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        atodoAdapter = new ItemAdapter(this, R.layout.list_item, todoItems);
    }

    private void readItems(){
        // Reference to special directory that this application is allowed to read from
        //File filesDir = getFilesDir();
        //File file = new File(filesDir, "todo.txt");
        db = db.getInstance(this);
        try {
            //todoItems = new ArrayList<String>(FileUtils.readLines(file));
            todoItems = db.getAllItems();

            //todoItems = new ArrayList<Item>(FileUtils.readLines(file));
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    /*
    private void writeItems(Item item){
        // Reference to special directory that this application is allowed to read from
        //File filesDir = getFilesDir();
        //File file = new File(filesDir, "todo.txt");

        try{
            //FileUtils.writeLines(file, todoItems);
        } catch (Exception e){

        }
    }*/

    // View that's passed in is the button
    public void onAddItem(View view) {
        Intent addIntent = new Intent(getBaseContext(), AddActivity.class);
        startActivityForResult(addIntent, ADD_REQUEST);
        //atodoAdapter.add()
        //atodoAdapter.add(etEditText.getText().toString());
        //etEditText.setText("");
    }

    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                todoItems.remove(pos);
                // If modify underlying data structure, need to notify adapter (else exception thrown and app crashes)
                atodoAdapter.notifyDataSetChanged();
                //writeItems();
                db.deleteItem(todoItems.get(pos));
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                Intent intent = new Intent(getBaseContext(), EditActivity.class);
                Bundle extras = new Bundle();
                Item prevItem = todoItems.get(pos);

                //extras.putString("prevText", todoItems.get(pos).toString());
                extras.putString("prevName", prevItem.name);
                extras.putString("prevPriority", prevItem.priority);
                extras.putString("prevDueDate", prevItem.dueDate);

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

        String newItemName;
        String newPriority;
        String newDueDate;

        String itemName;
        String priority;
        String dueDate;

        //super.onActivityResult(requestCode, resultCode, data);
        // Check which response we're responding to
        if (requestCode == EDIT_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                extras = data.getExtras();
                pos = extras.getInt("pos");

                itemName = extras.getString("newItemName");
                priority = extras.getString("newPriority");
                dueDate = extras.getString("newDueDate");

                Item updatedItem = new Item(itemName, priority, dueDate);

                todoItems.set(pos, updatedItem);

                atodoAdapter.notifyDataSetChanged();
                //writeItems();
                db.updateItem(updatedItem);
            }
        } else if (requestCode == ADD_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                extras = data.getExtras();

                itemName = extras.getString("itemName");
                priority = extras.getString("priority");
                dueDate = extras.getString("dueDate");

                Item newItem = new Item(itemName, priority, dueDate);

                todoItems.add(newItem);
                atodoAdapter.notifyDataSetChanged();
                //writeItems();
                db.addItem(newItem);
            }
        }
    }
}
