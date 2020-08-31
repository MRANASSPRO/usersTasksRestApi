package com.example.userstasksrestapi.service;

import com.example.userstasksrestapi.model.Task;
import com.example.userstasksrestapi.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiCaller {
    @GET("/users")
    Call<List<User>> getUserList();

    //@GET("/todos?userId={userId}")
    @GET("/todos")
    Call<List<Task>> getTaskByUser(@Query("userId") int userId);
}
