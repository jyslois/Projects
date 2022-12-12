package com.android.mymindnotes.model.retrofit;

import com.android.mymindnotes.model.UserInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JoinApi {
    @POST("/api/member/add")
    Call<Map<String, Object>> addUser(@Body UserInfo userinfo);
}
