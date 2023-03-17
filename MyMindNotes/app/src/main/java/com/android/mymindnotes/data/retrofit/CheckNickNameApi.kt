package com.android.mymindnotes.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CheckNickNameApi {
    @GET("/api/member/checkNickname/{nickname}")
    suspend fun checkNickname(
        @Path("nickname") nickname: String
    ): Map<String, Object>
}




