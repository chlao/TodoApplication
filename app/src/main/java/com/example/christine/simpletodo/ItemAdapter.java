package com.example.christine.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christine on 6/16/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item>{
    //private ArrayList<Item> items;

    /* Override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/
    public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        //this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView dueDateStr = (TextView) convertView.findViewById(R.id.due_date);
        TextView priority = (TextView) convertView.findViewById(R.id.priority);

        name.setText(item.name);
        dueDateStr.setText(item.dueDate);
        priority.setText(item.priority);

        switch (item.priority){
            case "HIGH":
                priority.setTextColor(Color.parseColor("#F44336"));
                break;
            case "MEDIUM":
                priority.setTextColor(Color.parseColor("#FFC107"));
                break;
            case "LOW":
                priority.setTextColor(Color.parseColor("#8BC34A"));
        }

        //dueDate.setText(item.dueDate);
        //SimpleDateFormat format = new SimpleDateFormat();
        //String dateToStr = format.format()

        return convertView;
    }
}
