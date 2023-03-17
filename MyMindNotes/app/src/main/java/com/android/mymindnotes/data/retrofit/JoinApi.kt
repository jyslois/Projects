package com.android.mymindnotes.data.retrofit

import retrofit2.http.POST
import com.android.mymindnotes.data.retrofit.model.UserInfo
import retrofit2.http.Body

interface JoinApi {
    @POST("/api/member/add")
    suspend fun addUser(
        @Body userinfo: UserInfo
    ): Map<String, Object>
}

