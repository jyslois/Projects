package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class GetUserInfoResponse(
    @SerializedName("code")
    override val code: Int? = 1,
    @SerializedName("msg")
    override val msg: String? = null,
    @SerializedName("birthyear")
    val birthyear: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String
): Response