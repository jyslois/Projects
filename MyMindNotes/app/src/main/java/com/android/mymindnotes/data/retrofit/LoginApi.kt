package com.android.mymindnotes.data.retrofit

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/member/login")
    suspend fun login(
        @Body userInfoLogin: UserInfoLogin
    ): Map<String, Object>
}