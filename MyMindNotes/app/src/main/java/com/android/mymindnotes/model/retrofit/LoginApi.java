package com.android.mymindnotes.model.retrofit;

import com.android.mymindnotes.model.UserInfoLogin;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("/api/member/login")
    Call<Map<String, Object>> login(@Body UserInfoLogin userinfologin);
}
