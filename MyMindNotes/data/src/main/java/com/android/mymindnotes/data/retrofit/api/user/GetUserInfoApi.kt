package com.android.mymindnotes.data.retrofit.api.user

import com.android.mymindnotes.core.dto.GetUserInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GetUserInfoApi {
    @GET("/api/member/getUserInfo/{user_index}")
    suspend fun getUserInfo(
        @Path("user_index") user_index: Int
    ): GetUserInfoResponse
}