package com.example.userstasksrestapi.service;

import com.example.userstasksrestapi.model.User;
import com.example.userstasksrestapi.model.UserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiCaller {
    @GET("/users")
    Call<List<User>> getUserList();
    //Call<UserList> getUserList(@Query("q")  String name);

}
