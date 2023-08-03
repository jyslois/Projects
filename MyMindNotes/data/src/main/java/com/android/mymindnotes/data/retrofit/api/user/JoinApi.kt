package com.android.mymindnotes.data.retrofit.api.user

import com.android.mymindnotes.core.dto.JoinResponse
import retrofit2.http.POST
import com.android.mymindnotes.core.dto.UserInfo
import retrofit2.http.Body

interface JoinApi {
    @POST("/api/member/add")
    suspend fun addUser(
        @Body userinfo: UserInfo
    ): JoinResponse
}

