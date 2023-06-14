package com.android.mymindnotes.data.retrofit.api.diary

import retrofit2.http.PUT
import com.android.mymindnotes.data.retrofit.model.diary.DiaryEdit
import retrofit2.http.Body
import retrofit2.http.Path

interface UpdateDiaryApi {
    @PUT("/api/diary/update/{diary_number}")
    suspend fun updateDiary(
        @Path("diary_number") diary_number: Int,
        @Body diaryEdit: DiaryEdit
    ): Map<String, Object>
}