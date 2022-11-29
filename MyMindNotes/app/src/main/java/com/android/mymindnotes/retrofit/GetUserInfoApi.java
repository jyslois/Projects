package com.android.mymindnotes.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetUserInfoApi {
    @GET("/api/member/getUserInfo/{user_index}")
    Call<Map<String, Object>> getUserInfo(@Path("user_index") int user_index);
}
