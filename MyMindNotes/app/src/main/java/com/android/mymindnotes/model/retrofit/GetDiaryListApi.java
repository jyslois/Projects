package com.android.mymindnotes.model.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDiaryListApi {
    @GET("/api/diary/getAll/{user_index}")
    Call<Map<String, Object>> getAllDiary(@Path("user_index") int user_index);
}
