package com.android.mymindnotes.data.retrofit.api.diary

import com.android.mymindnotes.core.model.DeleteDiaryResponse
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteDiaryApi {
    @DELETE("/api/diary/delete/{diary_number}")
    suspend fun deleteDiary(
        @Path("diary_number") diary_number: Int
    ): DeleteDiaryResponse
}