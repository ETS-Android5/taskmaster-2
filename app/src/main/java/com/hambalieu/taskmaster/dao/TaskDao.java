package com.hambalieu.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.hambalieu.taskmaster.model.Task;

@Dao
public interface TaskDao
{
    @Insert
    public void insert(Task task);


}
