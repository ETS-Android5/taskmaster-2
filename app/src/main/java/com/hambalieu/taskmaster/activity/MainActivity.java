package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.adapter.TaskListRecyclerViewAdapter;
import com.hambalieu.taskmaster.model.Task;
import com.hambalieu.taskmaster.model.State;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_DETAIL_TITLE_TASK_TAG = "Task Detail Title";
    public static final String TASK_BODY_TAG = "BODY";
    public static final String TASK_STATE_TAG = "STATE";
    public static final String USER_USERNAME_TAG = "userUsername";
    SharedPreferences preferences;
    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;
    List<Task> taskList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        buttonToGoAddTaskPage();
        buttonToGoAllTaskPage();
        buttonToGoSettingsPage();
        taskListRecyclerView();

    }

    @Override
    protected  void onResume(){
        super.onResume();
        String userNickname = preferences.getString(SettingsActivity.USER_NICKNAME_TAG, "No Nickname");
        ((TextView)findViewById(R.id.textViewDisplayUserNicknameMainActivity)).setText(getString(R.string.nickname, userNickname));

    }

    public void buttonToGoAddTaskPage(){
        Button buttonToAddTask = (Button)findViewById(R.id.buttonAddTaskMainActivity);
        buttonToAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTaskPage = new Intent(MainActivity.this , AddTaskActivity.class );
                startActivity(goToAddTaskPage);
            }
        });
    }

    public void buttonToGoAllTaskPage(){
        Button buttonToAllTask = (Button)findViewById(R.id.buttonAllTasksMainActivity);
        buttonToAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTaskPage = new Intent(MainActivity.this , AllTasksActivity.class );
                startActivity(goToAllTaskPage);
            }
        });
    }

    public void buttonToGoSettingsPage(){
        ImageButton buttonToSettingsPage = (ImageButton) findViewById(R.id.imageButtonToSettingsPageMainActivity);
        buttonToSettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSettingsPage = new Intent(MainActivity.this , SettingsActivity.class );
                startActivity(goToSettingsPage);
            }
        });
    }


    public void taskListRecyclerView(){
        RecyclerView taskDisplayRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewTaskMainActivity);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskDisplayRecyclerView.setLayoutManager(layoutManager);
        taskList.add(new Task("Homework", "finish lab work for each class", State.In_progress));
        taskList.add(new Task("Grocery", "Go to Safeway to get groceries for the week", State.New));
        taskList.add(new Task("Laundry", "switch clothes to dryer", State.In_progress));
        taskList.add(new Task("Workout", "completed workout for the day", State.Complete));
        taskList.add(new Task("Wash car", "go to car wash tomorrow", State.Assigned));

        taskListRecyclerViewAdapter = new TaskListRecyclerViewAdapter(taskList, this);
        taskDisplayRecyclerView.setAdapter(taskListRecyclerViewAdapter);

    }


}
