package com.android.mymindnotes.data.retrofit.api.user

import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteUserApi {
    @DELETE("/api/member/delete/{user_index}")
    suspend fun deleteUser(
        @Path("user_index") user_index: Int
    ): Map<String, Object>
}