package com.android.mymindnotes.data.retrofit.api.diary

import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import retrofit2.http.PUT
import com.android.mymindnotes.core.dto.DiaryEdit
import retrofit2.http.Body
import retrofit2.http.Path

interface UpdateDiaryApi {
    @PUT("/api/diary/update/{diary_number}")
    suspend fun updateDiary(
        @Path("diary_number") diary_number: Int,
        @Body diaryEdit: DiaryEdit
    ): UpdateDiaryResponse
}