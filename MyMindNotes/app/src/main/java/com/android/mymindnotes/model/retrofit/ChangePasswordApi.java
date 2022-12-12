package com.android.mymindnotes.model.retrofit;

import com.android.mymindnotes.model.ChangeUserPassword;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ChangePasswordApi {
    @PUT("/api/member/update/password")
    Call<Map<String, Object>> updateUserPassword(@Body ChangeUserPassword changeUserPassword);
}
