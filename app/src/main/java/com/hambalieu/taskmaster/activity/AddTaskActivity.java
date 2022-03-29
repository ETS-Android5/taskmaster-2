package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hambalieu.taskmaster.R;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        submittedTaskButton();

    }

    public void submittedTaskButton() {
        Button submitButton = (Button) findViewById(R.id.buttonAddTaskOnAddTaskActivity);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.textViewSubmittedOnAddTaskActivity)).setText("Submitted");

            }
        });
    }
}

