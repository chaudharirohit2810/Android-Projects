package com.example.floatingactionbutton;

import java.util.Date;

public class Task {
    private String name;
    private String Description;
    private String date;
    private int isOverdue;
    private int isCompleted;

    public Task(String name, String Description, String date, int isCompleted, int isOverdue) {
        this.name = name;
        this.Description = Description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.isOverdue = isOverdue;
    }

    public int getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(int isOverdue) {
        this.isOverdue = isOverdue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }
}
