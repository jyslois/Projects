package com.android.mymindnotes.model.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDiaryApi {
    @GET("/api/diary/get/{diary_number}")
    Call<Map<String, Object>> getDiary(@Path("diary_number") int diary_number);
}
