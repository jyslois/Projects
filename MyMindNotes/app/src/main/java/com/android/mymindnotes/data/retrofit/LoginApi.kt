package com.android.mymindnotes.data.retrofit

import com.android.mymindnotes.data.retrofit.model.UserInfoLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/member/login")
    suspend fun login(
        @Body userInfoLogin: UserInfoLogin
    ): Map<String, Object>
}