package com.android.mymindnotes.data.retrofit.api.user

import com.android.mymindnotes.core.dto.ChangePasswordResponse
import retrofit2.http.PUT
import com.android.mymindnotes.core.dto.ChangeUserPassword
import retrofit2.http.Body

interface ChangePasswordApi {
    @PUT("/api/member/update/password")
    suspend fun updateUserPassword(
        @Body changeUserPassword: ChangeUserPassword
    ): ChangePasswordResponse
}