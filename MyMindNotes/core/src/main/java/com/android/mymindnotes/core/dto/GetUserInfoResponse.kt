package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class GetUserInfoResponse(
    @SerializedName("birthyear")
    val birthyear: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String
)