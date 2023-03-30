package com.android.mymindnotes.data.retrofit.api.user

import retrofit2.http.PUT
import com.android.mymindnotes.data.retrofit.model.ChangeUserPassword
import retrofit2.http.Body

interface ChangePasswordApi {
    @PUT("/api/member/update/password")
    suspend fun updateUserPassword(
        @Body changeUserPassword: ChangeUserPassword
    ): Map<String, Object>
}