package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.hambalieu.taskmaster.R;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class TaskDetailActivity extends AppCompatActivity {

//    SharedPreferences preferences;
    public static final String TAG = "TaskDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        String imageS3Key = "";

        if(callingIntent != null)
        {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_DETAIL_TITLE_TASK_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
            imageS3Key = callingIntent.getStringExtra(MainActivity.TASK_IMAGE_TAG);



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

        if (imageS3Key != null && !imageS3Key.isEmpty()){
            String finalImageS3Key = imageS3Key;
            Amplify.Storage.downloadFile(
                    imageS3Key,
                    new File(getApplication().getFilesDir(),imageS3Key),
                    success -> {
                        ImageView viewTaskImageUpload = findViewById(R.id.imageViewImageAddTaskActivity);
                        viewTaskImageUpload.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> {
                        Log.e(TAG, "Unable to get image from s3 for: " + finalImageS3Key);
                    }
            );
        }

    }












}