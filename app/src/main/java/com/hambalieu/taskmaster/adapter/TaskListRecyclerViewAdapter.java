package com.hambalieu.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hambalieu.taskmaster.R;
import com.hambalieu.taskmaster.activity.MainActivity;
import com.hambalieu.taskmaster.activity.TaskDetailActivity;
import com.hambalieu.taskmaster.model.Task;

import java.util.List;

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder> {

    List<Task> taskList;
    Context callingActivity;


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
        TextView taskTextViewFragment = (TextView) holder.itemView.findViewById(R.id.textViewTaskFragment);
        String taskTitle = taskList.get(position).getTitle();
        String taskBody = taskList.get(position).getBody();
        String taskState = taskList.get(position).getState().toString();

        taskTextViewFragment.setText(taskList.get(position).getTitle());
        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetailPage = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailPage.putExtra(MainActivity.TASK_DETAIL_TITLE_TASK_TAG, taskTitle);
            goToTaskDetailPage.putExtra(MainActivity.TASK_BODY_TAG, taskBody);
            goToTaskDetailPage.putExtra(MainActivity.TASK_STATE_TAG, taskState);
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
