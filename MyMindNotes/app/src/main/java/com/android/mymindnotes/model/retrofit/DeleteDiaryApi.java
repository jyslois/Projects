package com.android.mymindnotes.model.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DeleteDiaryApi {
    @DELETE("/api/diary/delete/{diary_number}")
    Call<Map<String, Object>> deleteDiary(@Path("diary_number") int diary_number);
}
