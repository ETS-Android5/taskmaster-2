package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.adapter.TaskListRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "MainActivity";
    public static final String TASK_DETAIL_TITLE_TASK_TAG = "Task Detail Title";
    public static final String TASK_BODY_TAG = "Task Detail Body";
    public static final String TASK_STATE_TAG = "Task Detail State";

    public static final String USER_USERNAME_TAG = "userUsername";

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





//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        taskList = new ArrayList<>();
//

//
//        String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
//        com.amplifyframework.datastore.generated.model.Task testTask =
//                com.amplifyframework.datastore.generated.model.Task.builder()
//                    .title("Task name")
//                    .description("May task for the day")
//                    .dateCreated(new Temporal.DateTime(currentDateString))
//                .state(com.amplifyframework.datastore.generated.model.State.In_progress)
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(testTask), //making a graph Ql request
//                successResponse -> Log.i(TAG, "Main Activity.onCreate(): Made a Task successfully"),
//                failureResponse -> Log.i(TAG, "Main Activity.onCreate(): failed with this response" + failureResponse)
//
//        );
//        taskList.add(testTask);

//
//        Team team1 =
//                Team.builder()
//                    .teamName("Crud Depot")
//                    .build();
//        Amplify.API.mutate(
//                ModelMutation.create(team1), //making a graph Ql request
//                successResponse -> Log.i(TAG, "Main Activity.onCreate(): Made a team successfully"),
//                failureResponse -> Log.i(TAG, "Main Activity.onCreate(): team failed with this response" + failureResponse)
//
//        );
//
//
//        Team team2 =
//                Team.builder()
//                        .teamName("Hamshabb")
//                        .build();
//        Amplify.API.mutate(
//                ModelMutation.create(team2), //making a graph Ql request
//                successResponse -> Log.i(TAG, "Main Activity.onCreate(): Made a 2nd team successfully"),
//                failureResponse -> Log.i(TAG, "Main Activity.onCreate(): 2nd team failed with this response" + failureResponse)
//
//        );
//
//        Team team3 =
//                Team.builder()
//                        .teamName("Crud Alchemy")
//                        .build();
//        Amplify.API.mutate(
//                ModelMutation.create(team3), //making a graph Ql request
//                successResponse -> Log.i(TAG, "Main Activity.onCreate(): Made a 3rd team successfully"),
//                failureResponse -> Log.i(TAG, "Main Activity.onCreate(): 3rd team failed with this response" + failureResponse)
//
//        );
//
//
    }

    @Override
    protected  void onResume(){
        super.onResume();
        filteredTask();
        taskListRecyclerView();

    }
    private void filteredTask()
    {
        String userNickname = preferences.getString(SettingsActivity.USER_NICKNAME_TAG, "No Nickname");
        String teamName = preferences.getString(SettingsActivity.CHOOSE_TEAM_TAG, "No Team Name");
        ((TextView)findViewById(R.id.textViewDisplayUserNicknameMainActivity)).setText(getString(R.string.nickname, userNickname));

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
                    {
                        taskListRecyclerViewAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read products successfully!")
        );
    }

    private void init()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskList = new ArrayList<>();

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


        taskListRecyclerViewAdapter = new TaskListRecyclerViewAdapter(taskList, this);
        taskDisplayRecyclerView.setAdapter(taskListRecyclerViewAdapter);

    }


}
