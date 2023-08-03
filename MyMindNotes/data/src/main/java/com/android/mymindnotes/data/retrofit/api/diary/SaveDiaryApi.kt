package com.android.mymindnotes.data.retrofit.api.diary

import com.android.mymindnotes.core.dto.SaveDiaryResponse
import com.android.mymindnotes.core.dto.UserDiary
import retrofit2.http.POST
import retrofit2.http.Body

interface SaveDiaryApi {
    @POST("/api/diary/add")
    suspend fun addDiary(
        @Body userDiary: UserDiary
    ): SaveDiaryResponse
}