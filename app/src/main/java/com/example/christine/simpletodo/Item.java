package com.example.christine.simpletodo;

import java.util.Date;

/**
 * Created by Christine on 6/16/2016.
 */
public class Item {
    public String name;
    public String priority;
    public String dueDate;

    private static final int HIGH_PRIORITY_POS = 0;
    private static final int MEDIUM_PRIORITY_POS = 1;
    private static final int LOW_PRIORITY_POS = 2;

    private static final String HIGH_PRIORITY_STR = "HIGH";
    private static final String MEDIUM_PRIORITY_STR = "MEDIUM";
    private static final String LOW_PRIORITY_STR = "LOW";

    public Item(String name, String priority, String dueDate){
        this.name = name;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    /*
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
    }*/
}
