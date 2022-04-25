package com.hambalieu.taskmaster.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.predictions.models.LanguageType;
import com.hambalieu.taskmaster.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class TaskDetailActivity extends AppCompatActivity {

    public static final String TAG = "TaskDetailActivity";
    private Task taskToDelete = null;
    private CompletableFuture<Task> taskCompletableFuture = null;
    CompletableFuture<List<Team>> teamsFuture = null;


    private MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        mp = new MediaPlayer();
        taskCompletableFuture = new CompletableFuture<>();
        teamsFuture = new CompletableFuture<>();
        ;

        setUpUIElement();
        setUpSpeakButton();

    }
    private void setUpUIElement() {
        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        String latitude = null;
        String longitude = null;
        String imageS3Key = "";


        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_DETAIL_TITLE_TASK_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
            latitude = callingIntent.getStringExtra(MainActivity.TASK_LATITUDE_TAG);
            longitude = callingIntent.getStringExtra(MainActivity.TASK_LONGITUDE_TAG);
            imageS3Key = callingIntent.getStringExtra(MainActivity.TASK_IMAGE_TAG);

        }
        TextView taskDetailTextView = findViewById(R.id.textViewTaskTitleTaskDetailActivity);
        TextView taskDetailbodyTextView = findViewById(R.id.textViewBodyOnTaskDetailActivity);
        TextView taskDetailStateTextView = findViewById(R.id.textViewStateOnTaskDetailActivity);

        TextView taskDetailLatitudeTextView = findViewById(R.id.textViewLatitude);
        TextView taskDetailLongitudeTextView = findViewById(R.id.textViewLongitude);

        if (taskTitle != null) {
            taskDetailTextView.setText(taskTitle);

        } else {
            taskDetailTextView.setText(R.string.no_task);
        }

        taskDetailbodyTextView.setText(taskBody);
        taskDetailStateTextView.setText(taskState);

        if(latitude != null) {
            taskDetailLatitudeTextView.setText(latitude);
        } else {
            taskDetailLatitudeTextView.setText("No Location Latitude");
        }

        if(longitude != null) {
            taskDetailLongitudeTextView.setText(longitude);
        } else {
            taskDetailLongitudeTextView.setText("No Location Longitude");
        }



        if (imageS3Key != null && !imageS3Key.isEmpty()) {
            String finalImageS3Key = imageS3Key;
            Amplify.Storage.downloadFile(
                    imageS3Key,
                    new File(getApplication().getFilesDir(), imageS3Key),
                    success -> {
                        ImageView viewTaskImageUpload = findViewById(R.id.imageViewImageAddTaskActivity);
                        viewTaskImageUpload.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> Log.e(TAG, "Unable to get image from s3 for: " + finalImageS3Key)
            );
        }

    }

    private void setUpSpeakButton()
    {

        Button speakButton = (Button)findViewById(R.id.buttonSpeechGenerator);
        speakButton.setOnClickListener(b ->
        {
            String textToSpeakDescription = ((TextView)findViewById(R.id.textViewBodyOnTaskDetailActivity)).getText().toString();
            Amplify.Predictions.convertTextToSpeech(
                    textToSpeakDescription,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Conversion failed", error)
            );

            Amplify.Predictions.translateText(
                    textToSpeakDescription, LanguageType.ENGLISH, LanguageType.ARABIC,
                    result -> Log.i(TAG, result.getTranslatedText()),
                    error -> Log.e(TAG, "Translation failed", error)
            );

        });
    }

    private void setUpDeleteButton(){
        Button deleteButton = findViewById(R.id.buttonDeleteTaskOnTaskDetailPage);
        deleteButton.setOnClickListener(v ->
        {
            Amplify.API.mutate(
                    ModelMutation.delete(taskToDelete),
                    successResponse ->
                    {
                        Log.i(TAG, "EditTaskActivity.onCreate(): deleted a task successfully");
                        Intent goToMainActivity = new Intent(TaskDetailActivity.this, MainActivity.class);
                        startActivity(goToMainActivity);
                    },  // success callback
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
            );
        });
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e(TAG, "Error writing audio file", error);
        }
    }
}