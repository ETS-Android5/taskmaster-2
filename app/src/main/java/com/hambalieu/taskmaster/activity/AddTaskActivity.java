package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.model.State;
import com.hambalieu.taskmaster.model.Task;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        submittedTaskButton();

    }
        public void submittedTaskButton () {
            Button submitButton = (Button) findViewById(R.id.buttonAddTaskOnAddTaskActivity);

            Spinner taskStateSpinner = (Spinner) findViewById(R.id.spinnerTaskStateAddTaskActivity);
            taskStateSpinner.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    State.values()));

            submitButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    Task newTask = new Task(
                            ((EditText) findViewById(R.id.editTextSingleTaskInputOnAddTaskActivity)).getText().toString(),
                            ((EditText) findViewById(R.id.editTextInputDescriptionOnAddTaskActivity)).getText().toString(),
                            State.fromString(taskStateSpinner.getSelectedItem().toString()), new Date());

                    ((TextView) findViewById(R.id.textViewSubmittedOnAddTaskActivity)).setText("Submitted");
//                    taskMasterDatabase.taskDao().insert(newTask);

                }
            });
        }

}

