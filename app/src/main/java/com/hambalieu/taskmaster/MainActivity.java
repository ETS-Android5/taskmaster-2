package com.hambalieu.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_DETAIL_TITLE_TASK_TAG = "Task Detail Title";
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        buttonToGoAddTaskPage();
        buttonToGoAllTaskPage();
        buttonToGoSettingsPage();
        taskOneToTaskDetailPage();
        taskTwoToTaskDetailPage();
        taskThreeToTaskDetailPage();

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

    public void taskOneToTaskDetailPage(){
        TextView taskOne = (TextView) findViewById(R.id.textViewTask1MainActivity);
        taskOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetailPage = new Intent(MainActivity.this , TaskDetailActivity.class );
                startActivity(goToTaskDetailPage);
                String taskTitle = "Workout";
                SharedPreferences.Editor preferenceEditor = preferences.edit();
                preferenceEditor.putString(TASK_DETAIL_TITLE_TASK_TAG, taskTitle);
                preferenceEditor.apply();

            }
        });
    }


    public void taskTwoToTaskDetailPage(){
        TextView taskTwo = (TextView) findViewById(R.id.textViewTask2MainActivity);
        taskTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetailPage = new Intent(MainActivity.this , TaskDetailActivity.class );
                startActivity(goToTaskDetailPage);
                String taskTitle = "Do Homework";
                SharedPreferences.Editor preferenceEditor = preferences.edit();
                preferenceEditor.putString(TASK_DETAIL_TITLE_TASK_TAG, taskTitle);
                preferenceEditor.apply();

            }
        });
    }


    public void taskThreeToTaskDetailPage(){
        TextView taskThree = (TextView) findViewById(R.id.textViewMyTask3MainActivity);
        taskThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetailPage = new Intent(MainActivity.this , TaskDetailActivity.class );
                startActivity(goToTaskDetailPage);
                String taskTitle = "Go to Class";
                SharedPreferences.Editor preferenceEditor = preferences.edit();
                preferenceEditor.putString(TASK_DETAIL_TITLE_TASK_TAG, taskTitle);
                preferenceEditor.apply();

            }
        });
    }



}
