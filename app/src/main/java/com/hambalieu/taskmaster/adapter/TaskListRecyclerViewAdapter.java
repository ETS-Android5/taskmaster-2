package com.hambalieu.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.activity.MainActivity;
import com.hambalieu.taskmaster.activity.TaskDetailActivity;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder> {

    List<Task> taskList;
    Context callingActivity;

    public static final String TAG = "TaskListRecyclerViewAdapter";


    public TaskListRecyclerViewAdapter(List<Task> taskList, Context callingActivity) {
        this.taskList = taskList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListRecyclerViewAdapter.TaskListViewHolder holder, int position) {
        TextView taskTextViewFragment = holder.itemView.findViewById(R.id.textViewTaskFragment);
        Task task = taskList.get(position);
        DateFormat dateCreatedInputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateCreatedInputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat dateCreatedOutputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateCreatedOutputFormat.setTimeZone(TimeZone.getDefault());
        String dateCreatedString = "";

        try {
            {
                Date dateCreatedJavaDate = dateCreatedInputFormat.parse(task.getDateCreated().format());
                if (dateCreatedJavaDate != null)
                    dateCreatedString = dateCreatedOutputFormat.format(dateCreatedJavaDate);
            }
        } catch (ParseException pe) {
            Log.e(TAG, "Error converting product date to String: " + pe.getMessage(), pe);
        }

        taskTextViewFragment.setText(position + ". " + task.getTitle()
                + "\nDescription: " + task.getDescription()
                + "\nDate Created: " + dateCreatedString
                + "\nTeam Name: " + task.getTeam().getTeamName()
                + "\nTask State: " + task.getState().toString()
                + "\nTask Latitude: " + task.getTaskLatitude()
                + "\nTask Longitude: " + task.getTaskLongitude()
                +"\nTask Image: " + task.getTaskImageS3Key().toString());

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetailPage = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailPage.putExtra(MainActivity.TASK_DETAIL_TITLE_TASK_TAG, task.getTitle());
            goToTaskDetailPage.putExtra(MainActivity.TASK_BODY_TAG, task.getDescription());
            goToTaskDetailPage.putExtra(MainActivity.TASK_STATE_TAG, task.getState().toString());
            goToTaskDetailPage.putExtra(MainActivity.TASK_LATITUDE_TAG, task.getTaskLatitude().toString());
            goToTaskDetailPage.putExtra(MainActivity.TASK_LONGITUDE_TAG, task.getTaskLongitude().toString());
            goToTaskDetailPage.putExtra(MainActivity.TASK_IMAGE_TAG, task.getTaskImageS3Key().toString());
            callingActivity.startActivity(goToTaskDetailPage);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(View fragmentItemView) {
            super(fragmentItemView);
        }

    }
}
