package com.example.userstasksrestapi.service;

import com.example.userstasksrestapi.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApiCaller {
    @GET("/users")
    Call<List<User>> getUserList();
}
