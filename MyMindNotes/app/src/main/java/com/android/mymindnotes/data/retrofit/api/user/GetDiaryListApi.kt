package com.android.mymindnotes.data.retrofit.api.user

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetDiaryListApi {
    @GET("/api/diary/getAll/{user_index}")
    suspend fun getAllDiary(
        @Path("user_index") user_index: Int
    ): Map<String, Object>
}