package com.android.mymindnotes.model.retrofit;

import com.android.mymindnotes.data.retrofit.model.diary.DiaryEdit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UpdateDiaryApi {
    @PUT("/api/diary/update/{diary_number}")
    Call<Map<String, Object>> updateDiary(@Path("diary_number") int diary_number, @Body DiaryEdit diaryEdit);
}
