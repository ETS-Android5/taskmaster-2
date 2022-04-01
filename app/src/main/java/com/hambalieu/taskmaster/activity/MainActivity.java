package com.hambalieu.taskmaster.activity;

import static android.content.ContentValues.TAG;

import static org.slf4j.MDC.clear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;

import com.amplifyframework.datastore.generated.model.Task;
import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.adapter.TaskListRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // TODO: Change this to a Dynamo / GraphQl query

//       taskList = Amplify.API.query(
//                ModelQuery.list(Task.class),
//                success -> Log.i(TAG, "Read Task Successfully"),
//                failure -> Log.i(TAG, "Did not read tasks successfully")
//        );

        String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
        com.amplifyframework.datastore.generated.model.Task testTask =
                com.amplifyframework.datastore.generated.model.Task.builder()
                    .title("Task name")
                    .description("My task for the day")
                    .dateCreated(new Temporal.DateTime(currentDateString))
                .state(com.amplifyframework.datastore.generated.model.State.In_progress)
                .build();
        Amplify.API.mutate(
                ModelMutation.create(testTask), //making a graph Ql request
                successResponse -> Log.i(TAG, "Main Activity.onCreate(): Made a Task successfully"),
                failureResponse -> Log.i(TAG, "Main Activity.onCreate(): failed with this response" + failureResponse)

        );



        taskList = new ArrayList<>();
        taskList.add(testTask);

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

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->
                {
                    Log.i(TAG, "Read products successfully!");
                    taskList.clear();

                    for (Task databaseProduct : success.getData())
                    {
                        taskList.add(databaseProduct);
                    }

                    runOnUiThread(() ->
                    {
                        //adapter.products = products;
                        taskListRecyclerViewAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read products successfully!")
        );
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
