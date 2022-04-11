package com.hambalieu.taskmaster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.hambalieu.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "Edit Task";
    private Task taskToEdit = null;
    Spinner taskStateSpinner = null;
    Spinner teamSpinner = null;
    private CompletableFuture<Task> taskCompletableFuture = null;
    CompletableFuture<List<Team>> teamsFuture = null;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamsFuture = new CompletableFuture<>();
        activityResultLauncher = getImagePickingActivityResultLauncher();

//
//        setUpTeamSpinners();
        submittedEditTaskButton();
        setUpAddImageButton();
        setUpUIElementsEdit();
        setUpDeleteButton();
    }

    private void setUpUIElementsEdit() {
        Intent callingIntent = getIntent();
        String taskId = null;

        if (callingIntent != null)
        {
            taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_TAG);
        }

        String taskId2 = taskId;

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->
                {
                    Log.i(TAG, "Read task successfully!");

                    for (Task databaseTask : success.getData())
                    {
                        if (databaseTask.getId().equals(taskId2))
                        {
                            taskCompletableFuture.complete(databaseTask);
                        }
                    }

                    runOnUiThread(() ->
                    {
                        // Update UI elements
                    });
                },
                failure -> Log.i(TAG, "Did not read tasks successfully!")
        );

        try
        {
            taskToEdit = taskCompletableFuture.get();
        }
        catch (InterruptedException ie)
        {
            Log.e(TAG, "InterruptedException while getting task");
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException ee)
        {
            Log.e(TAG, "ExecutionException while getting task");
        }

        EditText taskTitleEditText = ((EditText) findViewById(R.id.editTextEditTaskTitle));
        EditText taskDescriptionEditText = ((EditText) findViewById(R.id.editTextEditTaskDescription));
        taskTitleEditText.setText(taskToEdit.getTitle());
        taskDescriptionEditText.setText(taskToEdit.getDescription());
        setUpTeamSpinners();

    }


    public void setUpTeamSpinners() {
        teamSpinner = findViewById(R.id.addTaskTeamspinner);
        taskStateSpinner = findViewById(R.id.spinnerTaskStateAddTaskActivity);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read teams successfully!");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();

                    for (Team team : success.getData()) {
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
//                            teamSpinner.getSelectedItem(getSpinnerIndex(teamSpinner, taskToEdit.getTeam().getTeamName()));
                            });
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully!");
                }
        );

        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));
        taskStateSpinner.setSelection(getSpinnerIndex(taskStateSpinner, taskToEdit.getState().toString()));


    }
    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck){
        for (int i = 0;i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)){
                return i;
            }
        }
        return 0;
    }


    public void submittedEditTaskButton() {
        Button submitButton = findViewById(R.id.buttonEditTaskFromTaskDetailPage);

        submitButton.setOnClickListener(view ->
        {
            saveEditask();
        });

        submitButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private void saveEditask() {
        String title = ((EditText) findViewById(R.id.editTextEditTaskTitle)).getText().toString();
        String description = ((EditText) findViewById(R.id.editTextEditTaskDescription)).getText().toString();
        String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
        String selectedTeamString = teamSpinner.getSelectedItem().toString();

        List<Team> teams = null;

        try
        {
            teams = teamsFuture.get();
        } catch (InterruptedException ie)
        {
            Log.e(TAG, "InterruptedException while getting teams");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting teams");
        }

        Team selectedTeam = teams.stream().filter(c -> c.getTeamName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

        Task newTask = Task.builder()
                .title(title)
                .description(description)
                .dateCreated(new Temporal.DateTime(new Date(), 0))
                .state((State) taskStateSpinner.getSelectedItem())
                .team(selectedTeam)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask), // making a GraphQL request to the cloud
                successResponse ->
                {
                    Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully");


                },// success callback
                failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse) // failure callback
        );


//        Intent goToHomeActivity = new Intent(EditTaskActivity.this, MainActivity.class);
//        startActivity(goToHomeActivity);
        //Snackbar.make(findViewById(R.id.textViewSubmittedOnAddTaskActivity), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        Toast.makeText(EditTaskActivity.this, "Task Updated", Toast.LENGTH_SHORT).show();

    }

    private void setUpDeleteButton(){
        Button deleteButton = findViewById(R.id.buttonEditTaskDelete);
        deleteButton.setOnClickListener(v ->
        {
            Amplify.API.mutate(
                    ModelMutation.delete(taskToEdit),
                    successResponse ->
                    {
                        Log.i(TAG, "EditTaskActivity.onCreate(): deleted a task successfully");
                        Intent goToHomeActivity = new Intent(EditTaskActivity.this, MainActivity.class);
                        startActivity(goToHomeActivity);
                    },  // success callback
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
            );
        });
    }

    private void setUpAddImageButton() {
        Button addImageButton = (Button) findViewById(R.id.buttonAddImageOnAddTaskActivity);
        addImageButton.setOnClickListener(b ->
        {
            launchImageSelectionIntent();
        });

    }

    private void launchImageSelectionIntent()
    {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);

    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename);
                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker!" + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                } else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String
            pickedImageFilename) {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {

                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());

                },
                failure ->
                {
                    Log.i(TAG, "Failure in uploading file uploaded to S3! with filename: " + pickedImageFilename + "with error: " + failure.getMessage());
                }
        );
    }


    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;

    }
}