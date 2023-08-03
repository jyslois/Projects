package com.android.mymindnotes.data.retrofit.api.user

import com.android.mymindnotes.core.dto.LoginResponse
import com.android.mymindnotes.core.dto.UserInfoLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/member/login")
    suspend fun login(
        @Body userInfoLogin: UserInfoLogin
    ): LoginResponse
}