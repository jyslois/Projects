package com.android.mymindnotes.data.retrofit.api.diary

import retrofit2.http.POST
import com.android.mymindnotes.data.retrofit.model.diary.UserDiary
import retrofit2.http.Body

interface SaveDiaryApi {
    @POST("/api/diary/add")
    suspend fun addDiary(
        @Body userDiary: UserDiary
    ): Map<String, Object>
}