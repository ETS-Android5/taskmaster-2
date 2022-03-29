package com.hambalieu.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hambalieu.taskmaster.dao.TaskDao;
import com.hambalieu.taskmaster.model.Task;

@Database(entities = {Task.class}, version = 1)

@TypeConverters({TaskmasterDatabaseConverters.class})
public abstract class TaskmasterDatabase extends RoomDatabase
{
    public abstract TaskDao taskDao();
}
