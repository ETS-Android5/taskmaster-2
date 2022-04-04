package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.hambalieu.taskmaster.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity
{
    public static final String TAG = "AddTaskActivity";
    Spinner teamSpinner = null;
    Spinner taskStateSpinner = null;
//    List<String>teamNames;
//    List<Team> teams;
    CompletableFuture<List<Team>> teamsFuture = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        teams = new ArrayList<>();

        teamsFuture = new CompletableFuture<List<Team>>();
        setUpTeamSpinners();
        submittedTaskButton();


    }

    public void setUpTeamSpinners()
    {
        teamSpinner = (Spinner) findViewById(R.id.addTaskTeamspinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read teams successfully!");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();

                    for (Team team  : success.getData())
                    {
                        teams.add(team);
                        teamNames.add(team.getTeamName());

                    }
                    teamsFuture.complete(teams);

                    runOnUiThread(() ->
                    {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames));
                    });
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully!");
                }
        );


        taskStateSpinner = (Spinner) findViewById(R.id.spinnerTaskStateAddTaskActivity);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));


    }

    public void submittedTaskButton() {
        Button submitButton = (Button) findViewById(R.id.buttonAddTaskOnAddTaskActivity);

        submitButton.setOnClickListener(view -> {

            String title = ((EditText) findViewById(R.id.editTextSingleTaskInputOnAddTaskActivity)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextInputDescriptionOnAddTaskActivity)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            String selectedNameFromSpinner = teamSpinner.getSelectedItem().toString();

            List<Team> teams = null;

            try {
                teams = teamsFuture.get();
            }
            catch (InterruptedException ie)
            {
                Log.e(TAG, "InterruptedException while getting teams");
            }
            catch (ExecutionException ee)
            {
              Log.e(TAG,"ExecutionException while getting teams");
            }

            Team selectedTeam = teams.stream().filter(c -> c.getTeamName().equals(selectedNameFromSpinner)).findAny().orElseThrow(RuntimeException::new);

            Task newTask = Task.builder()
                    .title(title)
                    .description(description)
                    .dateCreated(new Temporal.DateTime(currentDateString))
                    .state((State) taskStateSpinner.getSelectedItem())
                    .team(selectedTeam)
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

