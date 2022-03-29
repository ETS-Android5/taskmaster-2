package com.hambalieu.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hambalieu.taskmaster.model.Task;

import java.util.List;

@Dao
public interface TaskDao
{
    @Insert
    public void insert(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();

}
