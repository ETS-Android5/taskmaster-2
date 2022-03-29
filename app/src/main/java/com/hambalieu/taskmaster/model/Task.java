package com.hambalieu.taskmaster.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public  Long id;
    String title;
    String body;
    State state;
    java.util.Date dateCreated;



    public Task(String title, String body, State state, Date dateCreated)
    {
        this.title = title;
        this.body = body;
        this.state = state;
        this.dateCreated = dateCreated;
    }


    public Long getId() {
        return id;
    }





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated){
        this.dateCreated = dateCreated;
    }



}
