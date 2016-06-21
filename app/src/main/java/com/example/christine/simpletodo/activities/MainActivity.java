package com.example.christine.simpletodo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.christine.simpletodo.R;
import com.example.christine.simpletodo.models.Item;
import com.example.christine.simpletodo.adapters.ItemAdapter;
import com.example.christine.simpletodo.utils.ToDoItemDatabase;

import java.util.ArrayList;

import com.example.christine.simpletodo.R;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> todoItems;
    ItemAdapter atodoAdapter;

    TextView emptyMessage;

    ListView lvItems;
    ToDoItemDatabase db;

    CoordinatorLayout coordinatorLayout;

    public static final int EDIT_REQUEST = 5;
    public static final int ADD_REQUEST = 6;

    private static final String TAG = "MainActivity";

    @Override
    // XML layout for the activity is applied in onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.container);

        populateArrayItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(atodoAdapter);

        setupListViewListener();
    }

    public void populateArrayItems(){
        todoItems = new ArrayList<Item>();

        readItems();

        // Array adapter allows us to easily display the contents of an ArrayList w/in a ListView
        /** Paramters
         *  1. context - provides information (e.g. styles to be applied to views - instance of todo)
         *  2. resource file for each row (TextView)
         *  3. Arraylist
         */
        atodoAdapter = new ItemAdapter(this, R.layout.list_item, todoItems);
    }

    private void readItems(){
        db = db.getInstance(this);
        try {
            todoItems = db.getAllItems();

            isEmpty(todoItems);

        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void isEmpty(ArrayList<Item> todoItems){
        if (todoItems.isEmpty()){
            emptyMessage = new TextView(this);
            emptyMessage.setTextSize(getResources().getDimension(R.dimen.empty_list_text_size));
            coordinatorLayout.removeView(emptyMessage);
            emptyMessage.setText("Get started by adding a new task to do!");

            coordinatorLayout.addView(emptyMessage);
        }
    }


    // View that's passed in is the button
    public void onAddItem(View view) {
        Intent addIntent = new Intent(getBaseContext(), com.example.christine.simpletodo.activities.AddActivity.class);

        startActivityForResult(addIntent, ADD_REQUEST);
    }

    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                // MainActivity.this - Listener replaced this
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                final Item currItem = todoItems.get(pos);
                alert.setTitle("Delete entry");

                alert.setMessage(R.string.deletion_confirmation)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteItem(currItem);

                                todoItems.remove(currItem);
                                // If modify underlying data structure, need to notify adapter (else exception thrown and app crashes)
                                atodoAdapter.notifyDataSetChanged();

                                isEmpty(todoItems);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                            }
                        });

                alert.show();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                Intent intent = new Intent(getBaseContext(), com.example.christine.simpletodo.activities.EditActivity.class);
                Bundle extras = new Bundle();
                Item prevItem = todoItems.get(pos);

                extras.putString("prevName", prevItem.name);
                extras.putString("prevPriority", prevItem.priority);
                extras.putString("prevDueDate", prevItem.dueDate);

                extras.putInt("pos", pos);
                intent.putExtras(extras);

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

        // Check which response we're responding to
        if (requestCode == EDIT_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                extras = data.getExtras();
                pos = extras.getInt("pos");

                newItemName = extras.getString("newItemName");
                newPriority = extras.getString("newPriority");
                newDueDate = extras.getString("newDueDate");

                Item updatedItem = new Item(newItemName, newPriority, newDueDate);

                db.updateItem(todoItems.get(pos), updatedItem);

                todoItems.set(pos, updatedItem);
                atodoAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == ADD_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                if (emptyMessage != null){
                    coordinatorLayout.removeView(emptyMessage);
                }

                extras = data.getExtras();

                itemName = extras.getString("itemName");
                priority = extras.getString("priority");
                dueDate = extras.getString("dueDate");

                Item newItem = new Item(itemName, priority, dueDate);

                todoItems.add(newItem);
                atodoAdapter.notifyDataSetChanged();

                db.addItem(newItem);
            }
        }
    }
}
