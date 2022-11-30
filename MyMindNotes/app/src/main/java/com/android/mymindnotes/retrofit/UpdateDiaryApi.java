package com.android.mymindnotes.retrofit;

import com.android.mymindnotes.model.DiaryEdit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UpdateDiaryApi {
    @PUT("/api/diary/update/{diary_number}")
    Call<Map<String, Object>> updateDiary(@Path("diary_number") int diary_number, @Body DiaryEdit diaryEdit);
}
