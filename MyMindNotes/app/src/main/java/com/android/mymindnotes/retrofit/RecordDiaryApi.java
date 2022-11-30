package com.android.mymindnotes.retrofit;

import com.android.mymindnotes.model.UserDiary;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecordDiaryApi {
    @POST("/api/diary/add")
    Call<Map<String, Object>> addDiary(@Body UserDiary userDiary);
}
