package com.android.mymindnotes.data.retrofit.api.diary

import com.android.mymindnotes.core.model.UserDiary
import retrofit2.http.POST
import retrofit2.http.Body

interface SaveDiaryApi {
    @POST("/api/diary/add")
    suspend fun addDiary(
        @Body userDiary: UserDiary
    ): Map<String, Object>
}