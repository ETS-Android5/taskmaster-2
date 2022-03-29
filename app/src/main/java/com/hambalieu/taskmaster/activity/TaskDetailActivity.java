package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.activity.MainActivity;

public class TaskDetailActivity extends AppCompatActivity {

    SharedPreferences preferences;

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
        TextView taskDetailTextView = (TextView) findViewById(R.id.textViewTaskTitleTaskDetailActivity);
        TextView taskDetailbodyTextView = (TextView) findViewById(R.id.textViewBodyOnTaskDetailActivity);
        TextView taskDetailStateTextView = (TextView) findViewById(R.id.textViewStateOnTaskDetailActivity);
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
    }


}