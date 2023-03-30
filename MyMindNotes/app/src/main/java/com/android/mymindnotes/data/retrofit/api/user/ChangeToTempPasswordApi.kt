package com.android.mymindnotes.data.retrofit.api.user

import retrofit2.http.PUT
import com.android.mymindnotes.data.retrofit.model.user.ChangeToTemporaryPassword
import retrofit2.http.Body

interface ChangeToTempPasswordApi {
    @PUT("/api/member/update/password/temp")
    suspend fun toTemPassword(
        @Body changeToTemporaryPassword: ChangeToTemporaryPassword
    ): Map<String, Object>
}