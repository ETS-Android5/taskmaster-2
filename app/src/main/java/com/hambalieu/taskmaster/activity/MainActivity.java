package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;


import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;


import com.amplifyframework.datastore.generated.model.Task;

import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.adapter.TaskListRecyclerViewAdapter;


import java.util.ArrayList;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String TASK_ID_TAG = "Task Id";
    public final String TAG = "MainActivity";

    public static final String TASK_DETAIL_TITLE_TASK_TAG = "Task Detail Title";
    public static final String TASK_BODY_TAG = "Task Detail Body";
    public static final String TASK_STATE_TAG = "Task Detail State";
    public static final String TASK_LATITUDE_TAG = "Task Detail Latitude";
    public static final String TASK_LONGITUDE_TAG = "Task Detail Longitude";
    public static final String TASK_IMAGE_TAG = "TaskDetailActivity";



//    public static final String USER_USERNAME_TAG = "userUsername";

    SharedPreferences preferences;
    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;
    List<Task> taskList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        buttonToGoAddTaskPage();
        buttonToGoAllTaskPage();
        buttonToGoSettingsPage();
        taskListRecyclerView();
        setUpLoginAndLogoutButton();

    }

    @Override
    protected  void onResume(){
        super.onResume();
        AnalyticsEvent event =AnalyticsEvent.builder()
                .name("resumedApp")
                .addProperty("timeResumed", Long.toString(new Date().getTime()))
                .addProperty("eventDescription", "Resumed MainActivity")
                .build();

        Amplify.Analytics.recordEvent(event);


        filteredTask();
        taskListRecyclerView();


        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String username = "";
        if (authUser == null)
        {
            Button loginButton = findViewById(R.id.taskListLoginButton);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = findViewById(R.id.taskListLogoutButton);
            logoutButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            Log.i(TAG, "Username is: " + username);
            Button loginButton = findViewById(R.id.taskListLoginButton);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = findViewById(R.id.taskListLogoutButton);
            logoutButton.setVisibility(View.VISIBLE);


            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "Fetch user attributes succeeded for username: " + username);

                        for (AuthUserAttribute userAttribute : success)
                        {
                            if (userAttribute.getKey().getKeyString().equals("nickname"))
                            {
                                String userNickname = userAttribute.getValue();
                                runOnUiThread(() ->
                                        ((TextView)findViewById(R.id.textViewDisplayUserNicknameMainActivity)).setText(userNickname)
                                );
                            }
                        }
                    },
                    failure ->
                            Log.i(TAG, "Fetch user attributes failed: " + failure)
            );
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private void filteredTask()
    {
        String teamName = preferences.getString(SettingsActivity.CHOOSE_TEAM_TAG, "No Team Name");

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->
                {
                    Log.i(TAG, "Read products successfully!");
                    taskList.clear();

                    for (Task task: success.getData())
                    {
                        if(task.getTeam().getTeamName().equals(teamName))
                        {
                            taskList.add(task);
                        }

                    }

                    runOnUiThread(() ->
                            taskListRecyclerViewAdapter.notifyDataSetChanged());
                },
                failure -> Log.i(TAG, "Did not read products successfully!")
        );
    }

    private void init()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskList = new ArrayList<>();

        //Analytics Part of the code
        AnalyticsEvent event =AnalyticsEvent.builder()
                .name("openedApp")
                .addProperty("timeOpened", Long.toString(new Date().getTime()))
                .addProperty("eventDescription", "Opened MainActivity")
                .build();

        Amplify.Analytics.recordEvent(event);

    }


    public void buttonToGoAddTaskPage(){
        Button buttonToAddTask = findViewById(R.id.buttonAddTaskMainActivity);
        buttonToAddTask.setOnClickListener(view -> {
            Intent goToAddTaskPage = new Intent(MainActivity.this , AddTaskActivity.class );
            startActivity(goToAddTaskPage);
        });
    }

    public void buttonToGoAllTaskPage(){
        Button buttonToAllTask = findViewById(R.id.buttonAllTasksMainActivity);
        buttonToAllTask.setOnClickListener(view -> {
            Intent goToAllTaskPage = new Intent(MainActivity.this , AllTasksActivity.class );
            startActivity(goToAllTaskPage);
        });
    }

    public void buttonToGoSettingsPage(){
        ImageButton buttonToSettingsPage =  findViewById(R.id.imageButtonToSettingsPageMainActivity);
        buttonToSettingsPage.setOnClickListener(view -> {
            Intent goToSettingsPage = new Intent(MainActivity.this , SettingsActivity.class );
            startActivity(goToSettingsPage);
        });
    }


    public void taskListRecyclerView(){
        RecyclerView taskDisplayRecyclerView = findViewById(R.id.recyclerViewTaskMainActivity);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskDisplayRecyclerView.setLayoutManager(layoutManager);


        taskListRecyclerViewAdapter = new TaskListRecyclerViewAdapter(taskList, this);
        taskDisplayRecyclerView.setAdapter(taskListRecyclerViewAdapter);

    }

    private void setUpLoginAndLogoutButton()
    {
        Button loginButton = findViewById(R.id.taskListLoginButton);
        loginButton.setOnClickListener(v ->
        {
            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogInIntent);
        });

        Button logoutButton = findViewById(R.id.taskListLogoutButton);
        logoutButton.setOnClickListener(v ->
                Amplify.Auth.signOut(
                        () ->
                        {
                            Log.i(TAG, "Logout succeeded!");
                            runOnUiThread(() ->
                                    ((TextView) findViewById(R.id.textViewDisplayUserNicknameMainActivity)).setText("")
                            );
                            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(goToLogInIntent);
                        },
                        failure ->
                        {
                            Log.i(TAG, "Logout failed: " + failure);
                            runOnUiThread(() ->
                                    Toast.makeText(MainActivity.this, "Log out failed!", Toast.LENGTH_SHORT).show());
                        }
                ));
    }


}
