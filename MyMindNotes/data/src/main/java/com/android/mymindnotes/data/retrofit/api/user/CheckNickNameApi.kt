package com.android.mymindnotes.data.retrofit.api.user

import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CheckNickNameApi {
    @GET("/api/member/checkNickname/{nickname}")
    suspend fun checkNickname(
        @Path("nickname") nickname: String
    ): DuplicateCheckResponse
}




