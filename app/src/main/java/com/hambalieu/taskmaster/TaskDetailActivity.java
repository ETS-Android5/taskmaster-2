package com.hambalieu.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected  void onResume(){
        super.onResume();
        String taskTitle = preferences.getString(MainActivity.TASK_DETAIL_TITLE_TASK_TAG, "Empty");
        ((TextView)findViewById(R.id.textViewTaskTitleTaskDetailActivity)).setText(getString(R.string.task_title_task_detail_activity, taskTitle));

    }

}