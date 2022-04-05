package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.hambalieu.taskmaster.R;


public class TaskDetailActivity extends AppCompatActivity {

//    SharedPreferences preferences;
    public static final String TAG = "TaskDetailActivity";
    private final Task taskToDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;

        if(callingIntent != null)
        {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_DETAIL_TITLE_TASK_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
        }
        TextView taskDetailTextView = findViewById(R.id.textViewTaskTitleTaskDetailActivity);
        TextView taskDetailbodyTextView = findViewById(R.id.textViewBodyOnTaskDetailActivity);
        TextView taskDetailStateTextView =  findViewById(R.id.textViewStateOnTaskDetailActivity);
        if(taskTitle != null)
        {
            taskDetailTextView.setText(taskTitle);

        }
        else
        {
            taskDetailTextView.setText(R.string.no_task);
        }

        taskDetailbodyTextView.setText(taskBody);
        taskDetailStateTextView.setText(taskState);
        setUpDeleteButton();
    }


    private void setUpDeleteButton()
    {
        Button deleteButton = findViewById(R.id.deleteTaskButton);
        deleteButton.setOnClickListener(v ->
        {
            assert false;
            Amplify.API.mutate(
                    ModelMutation.delete(taskToDelete),
                    successResponse ->
                    {
                        Log.i(TAG, "TaskDetailActivity.onCreate(): deleted a task successfully");

                        Intent goToTaskListActivity = new Intent(TaskDetailActivity.this, MainActivity.class);
                        startActivity(goToTaskListActivity);
                    },
                    failureResponse -> Log.i(TAG, "TaskDetailActivity.onCreate(): failed with this response: " + failureResponse)
            );
        });
    }




}