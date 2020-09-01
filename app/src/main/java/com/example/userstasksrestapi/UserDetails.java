package com.example.userstasksrestapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        super.onCreate(savedInstanceState);
        Context mContext = getApplicationContext();
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        coordinatorLayoutTask = (CoordinatorLayout) findViewById(R.id.coordinator_layout2);
        recyclerViewTask = (RecyclerView) findViewById(R.id.recycler_tasks_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(layoutManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //getting the intent passed in from main activity
        final User user = (User) getIntent().getSerializableExtra("user");

        //checking for internet connectivity
        if (!isNetworkAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutTask, "No Network connection",
                            Snackbar.LENGTH_LONG)
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
}

