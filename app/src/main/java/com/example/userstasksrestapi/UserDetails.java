package com.example.userstasksrestapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userstasksrestapi.model.Task;
import com.example.userstasksrestapi.model.User;
import com.example.userstasksrestapi.service.RestApiBuilder;
import com.example.userstasksrestapi.service.RestApiCaller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserDetails extends AppCompatActivity {

    private TextView taskTitle, taskCompleted;
    private RecyclerView recyclerViewTask;
    private TaskAdapter adapterTask;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerViewTask = (RecyclerView) findViewById(R.id.recycler_tasks_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        taskTitle = (TextView) findViewById(R.id.taskTitle);
        taskCompleted = (TextView) findViewById(R.id.taskCompleted);

        //getting intent extra
        final User user = (User) getIntent().getSerializableExtra("user");
        //Task task = (Task) getIntent().getSerializableExtra("task");

        /*
        taskTitle.setText(getString(R.string.taskTitle, task.getTitle()));
        taskCompleted.setText(String.valueOf(task.isCompleted()));
        */

        //checking for network connectivity
        if (!isNetworkAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No Network connection", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fetchUsersTasks(user.getId());
                        }
                    });

            snackbar.show();
        } else {
            fetchUsersTasks(user.getId());
        }
    }

    private void prepareData(List<Task> taskList) {
        adapterTask = new TaskAdapter(taskList);
        recyclerViewTask.setAdapter(adapterTask);

    }

    private void fetchUsersTasks(int id) {
        int searchParams = id;
        RestApiCaller apiService = new RestApiBuilder().getService();
        Call<List<Task>> TaskListCall = apiService.getTaskByUser(id);

        TaskListCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    List<Task> taskList = response.body();
                    prepareData(taskList);
                } else {

                    Toast.makeText(UserDetails.this,
                            "Bad Request!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(UserDetails.this,
                        "Request Failed!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private CustomTabsIntent getCustomTabIntentInstance() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        return builder.build();
    }

}

