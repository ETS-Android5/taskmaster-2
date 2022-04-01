package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.google.android.material.snackbar.Snackbar;
import com.hambalieu.taskmaster.R;


import java.util.Date;

public class AddTaskActivity extends AppCompatActivity
{
    public static final String TAG = "AddTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        submittedTaskButton();

    }

    public void submittedTaskButton() {
        Button submitButton = (Button) findViewById(R.id.buttonAddTaskOnAddTaskActivity);

        Spinner taskStateSpinner = (Spinner) findViewById(R.id.spinnerTaskStateAddTaskActivity);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));

        submitButton.setOnClickListener(view -> {

            String title = ((EditText) findViewById(R.id.editTextSingleTaskInputOnAddTaskActivity)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextInputDescriptionOnAddTaskActivity)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            Task newTask = Task.builder()
                    .title(title)
                    .description(description)
                    .dateCreated(new Temporal.DateTime(currentDateString))
                    .state((State) taskStateSpinner.getSelectedItem())
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask), // making a GraphQL request to the cloud
                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"), // success callback
                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse) // failure callback
            );

            Snackbar.make(findViewById(R.id.textViewSubmittedOnAddTaskActivity), "Task Saved!", Snackbar.LENGTH_SHORT).show();

        });
    }

}

