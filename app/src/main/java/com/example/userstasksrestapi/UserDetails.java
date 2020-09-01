package com.example.userstasksrestapi;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.userstasksrestapi.model.Task;
import com.example.userstasksrestapi.model.User;
import com.example.userstasksrestapi.service.RestApiBuilder;
import com.example.userstasksrestapi.service.RestApiService;
import com.example.userstasksrestapi.utils.Utils;
import com.example.userstasksrestapi.view.TaskAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserDetails extends AppCompatActivity {

    private RecyclerView recyclerViewTask;
    private TaskAdapter adapterTask;
    private CoordinatorLayout coordinatorLayoutTask;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create and launch the second activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        coordinatorLayoutTask = (CoordinatorLayout) findViewById(R.id.coordinator_layout2);
        recyclerViewTask = (RecyclerView) findViewById(R.id.recycler_tasks_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
                2);
        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(layoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //get the intent from main activity
        final User user = (User) getIntent().getSerializableExtra("user");

        //check for network connectivity
        if (!Utils.isConnected(getApplicationContext())) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutTask, "No Network connection",
                            Snackbar.LENGTH_LONG)

                    //request tasks data when offline
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

    //set the tasks data on the view with an adapter
    private void prepareData(List<Task> taskList) {
        adapterTask = new TaskAdapter(taskList);
        recyclerViewTask.setAdapter(adapterTask);

    }

    private void fetchUsersTasks(int id) {
        //retrieve the user id and save it
        int queryParams = id;
        RestApiService apiService = new RestApiBuilder(this).getService();
        Call<List<Task>> TaskListCall = apiService.getTaskByUser(queryParams);

        TaskListCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    List<Task> taskList = response.body();
                    prepareData(taskList);
                } else {

                    //request format error
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
}