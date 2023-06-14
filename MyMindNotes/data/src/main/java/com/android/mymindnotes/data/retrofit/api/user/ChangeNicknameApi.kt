package com.android.mymindnotes.data.retrofit.api.user

import retrofit2.http.PUT
import com.android.mymindnotes.data.retrofit.model.user.ChangeUserNickname
import retrofit2.http.Body

interface ChangeNicknameApi {
    @PUT("/api/member/update/nickname")
    suspend fun updateUserNickname(
        @Body changeUserNickname: ChangeUserNickname
    ): Map<String, Object>
}