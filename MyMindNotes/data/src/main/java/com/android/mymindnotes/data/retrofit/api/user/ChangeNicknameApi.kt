package com.android.mymindnotes.data.retrofit.api.user

import retrofit2.http.PUT
import com.android.mymindnotes.core.dto.ChangeUserNickname
import com.android.mymindnotes.core.dto.ChangeNicknameResponse
import retrofit2.http.Body

interface ChangeNicknameApi {
    @PUT("/api/member/update/nickname")
    suspend fun updateUserNickname(
        @Body changeUserNickname: ChangeUserNickname
    ): ChangeNicknameResponse
}