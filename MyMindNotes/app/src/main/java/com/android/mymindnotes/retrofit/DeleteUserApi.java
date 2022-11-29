package com.android.mymindnotes.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DeleteUserApi {
    @DELETE("/api/member/delete/{user_index}")
    Call<Map<String, Object>> deleteUser(@Path("user_index") int user_index);
}
