package com.hambalieu.taskmaster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.hambalieu.taskmaster.R;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    Spinner teamSpinner = null;
    Spinner taskStateSpinner = null;
    String imageS3key = "";


    CompletableFuture<List<Team>> teamsFuture = null;



    FusedLocationProviderClient locationProviderClient = null;

    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        teamsFuture = new CompletableFuture<>();
        activityResultLauncher = getImagePickingActivityResultLauncher();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG ,"Application does not have access to either ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION" );
            return;
        }

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProviderClient.flushLocations();
        locationProviderClient.getLastLocation().addOnSuccessListener(location ->
        {
            Log.i(TAG, "Our latitude: " + location.getLatitude());
            Log.i(TAG, "Our longitude: " + location.getLongitude());
        });



        setUpCallingIntent();
        setUpTeamSpinners();
        submittedTaskButton();
        setUpAddImageButton();
        setUpDeleteImageButton();
        updateImageButtons();
    }

    private void setUpCallingIntent(){
        Intent callingIntent = getIntent();

        if ((callingIntent != null) && (callingIntent.getType() != null)
                && (callingIntent.getType().startsWith("image")))
        {
            Uri incomingImageFileUri = callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (incomingImageFileUri != null)
            {
                InputStream incomingImageFileInputStream = null;
                try
                {
                    incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);
                    String pickedImageFilename = getFileNameFromUri(incomingImageFileUri);
                    uploadInputStreamToS3(incomingImageFileInputStream, pickedImageFilename, incomingImageFileUri);


                } catch (FileNotFoundException fnfe) {
                    Log.e(TAG, "Could not save calling intent image" + fnfe);
                }

                ImageView taskImageView = findViewById(R.id.imageViewImageAddTaskActivity);
                taskImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));

            }
        }

    }


    public void setUpTeamSpinners() {
        teamSpinner = findViewById(R.id.addTaskTeamspinner);

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
                            teamSpinner.setAdapter(new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    teamNames)));
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully!");
                }
        );


        taskStateSpinner = findViewById(R.id.spinnerTaskStateAddTaskActivity);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));


    }

    public void submittedTaskButton() {
        Button submitButton = findViewById(R.id.buttonEditTaskFromTaskDetailPage);

        submitButton.setOnClickListener(view ->
        {
            saveTask();
        });

        submitButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private void saveTask() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG ,"Application does not have access to either ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION" );
            return;
        }

        String title = ((EditText) findViewById(R.id.editTextSingleTaskInputOnAddTaskActivity)).getText().toString();
        String description = ((EditText) findViewById(R.id.editTextInputDescriptionOnAddTaskActivity)).getText().toString();
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

        locationProviderClient.getLastLocation().addOnSuccessListener(location ->
        {
            if(location == null)
            {
                Log.e(TAG, "Location callback was null");

            }
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            Log.i(TAG, "Our latitude: " + location.getLatitude());
            Log.i(TAG, "Our longitude: " + location.getLongitude());
            savedTask(title, description, currentLatitude, currentLongitude, selectedTeam);

        }
        ).addOnCanceledListener(()-> {
            Log.e(TAG, "Location request was canceled");})
        .addOnFailureListener(failure -> {
            Log.e(TAG, "Location request failed Error was: " + failure.getMessage(), failure.getCause());})
        .addOnCompleteListener(complete -> {
            Log.e(TAG, "Location request completed");
        });


//        String currentLatitude = "";
//        String currentLongitude = "";
//        try
//        {
//            currentLatitude = latitudeFuture.get();
//            currentLongitude = longitudeFuture.get();
//        }
//        catch (InterruptedException ie)
//        {
//            Log.e(TAG, "InterruptedException while getting Lat/long");
//            Thread.currentThread().interrupt();
//        }
//        catch (ExecutionException ee)
//        {
//            Log.e(TAG, "ExecutionException while getting Lat/Long");
//        }


    }

    private void setUpAddImageButton() {
        Button addImageButton = (Button) findViewById(R.id.buttonAddImageOnAddTaskActivity);
        addImageButton.setOnClickListener(view -> launchImageSelectionIntent());

    }

    private void launchImageSelectionIntent() {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "image/jpg"});
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
                                    if (result.getData() != null) {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                                        } catch (FileNotFoundException fnfe) {
                                            Log.e(TAG, "Could not get file from file picker!" + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String
            pickedImageFilename, Uri pickedImageFileUri) {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {
                    imageS3key = success.getKey();
                    updateImageButtons();
                    saveTask();
                    ImageView taskImageView = findViewById(R.id.imageViewImageAddTaskActivity);
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    InputStream pickedImageInputStreamUpload = null;
                    try
                    {
                        pickedImageInputStreamUpload = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file from Uri!" + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamUpload));

                },
                failure ->
                {
                    Log.i(TAG, "Failure in uploading file uploaded to S3! with filename: " + pickedImageFilename + "with error: " + failure.getMessage());
                }
        );
    }

    private void setUpDeleteImageButton() {
        Button deleteImageButton = (Button) findViewById(R.id.buttonDeleteImageOnAddTaskActiviyPage);
        deleteImageButton.setOnClickListener(v ->
        {
            deleteImageFromS3();
        });
    }

    private void deleteImageFromS3() {
        if (!imageS3key.isEmpty()) {
            Amplify.Storage.remove(
                    imageS3key,
                    success ->
                    {
                        imageS3key = "";
                        ImageView taskImageView = findViewById(R.id.imageViewImageAddTaskActivity);

                        runOnUiThread(() ->
                                {
                                    taskImageView.setImageResource(android.R.color.transparent);
                                }
                        );

                        updateImageButtons();
                        Log.i(TAG, "Succeeded in deleting file on S3! Key is: " + success.getKey());
                    },
                    failure ->
                    {
                        Log.e(TAG, "Failure in deleting file on S3 with key: " + imageS3key + " with error: " + failure.getMessage());
                    }
            );
        }
    }


    private void updateImageButtons() {
        Button addImageButton = (Button) findViewById(R.id.buttonAddImageOnAddTaskActivity);
        Button deleteImageButton = (Button) findViewById(R.id.buttonDeleteImageOnAddTaskActiviyPage);

        if (imageS3key.isEmpty()) {
            runOnUiThread(() ->
                    {
                        deleteImageButton.setVisibility(View.INVISIBLE);
                        addImageButton.setVisibility(View.VISIBLE);
                    }
            );
        } else {
            runOnUiThread(() ->
                    {
                        deleteImageButton.setVisibility(View.VISIBLE);
                        addImageButton.setVisibility(View.INVISIBLE);
                    }
            );
        }
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

    private void savedTask(String title, String description, String latitude, String longitude, Team selectedTeam)
    {

        Task newTask = Task.builder()
                .title(title)
                .description(description)
                .dateCreated(new Temporal.DateTime(new Date(), 0))
                .state((State) taskStateSpinner.getSelectedItem())
                .team(selectedTeam)
                .taskLatitude(latitude)
                .taskLongitude(longitude)
                .taskImageS3Key(imageS3key)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask), // making a GraphQL request to the cloud
                successResponse ->
                {
                    Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully");

                },
                failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse) // failure callback
        );

        Toast.makeText(AddTaskActivity.this, "Task Saved", Toast.LENGTH_SHORT).show();
    }

}

//    @Override
//    protected void onResume() {
//        super.onResume();
////        Intent callingIntent = getIntent();
////        if ((callingIntent != null) && (callingIntent.getType() != null)
////                && (callingIntent.getType().equals("text/plain"))) {
////            String callingText = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
////            if (callingText != null) {
////                ((EditText) findViewById(R.id.editTextSingleTaskInputOnAddTaskActivity)).setText(callingText);
////            }
////
////        }
//
//    }


