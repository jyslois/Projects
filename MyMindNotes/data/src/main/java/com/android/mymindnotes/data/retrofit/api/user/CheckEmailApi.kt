package com.android.mymindnotes.data.retrofit.api.user

import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CheckEmailApi {
    // HTTP method 중 GET 사용: Get 방식으로 연동이 이루어져야하며 baseUrl 뒤에 Path로 "/api/member/checkEmail/{email}" 지정
    @GET("/api/member/checkEmail/{email}")
    suspend fun checkEmail(
        @Path("email") email: String
    ): DuplicateCheckResponse
}