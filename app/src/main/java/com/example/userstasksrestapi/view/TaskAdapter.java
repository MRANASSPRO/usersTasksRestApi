package com.example.userstasksrestapi.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.userstasksrestapi.R;
import com.example.userstasksrestapi.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskCompleted;

        public MyViewHolder(View view) {
            super(view);
            taskTitle = (TextView) view.findViewById(R.id.taskTitle);
            taskCompleted = (TextView) view.findViewById(R.id.taskCompleted);
        }
    }

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.taskslist_item, viewGroup, false);

        return new TaskAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Task task = taskList.get(i);

        myViewHolder.taskTitle.setText(task.getTitle());
        myViewHolder.taskCompleted.setText(String.valueOf(task.isCompleted()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
