package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    override val code: Int?,
    @SerializedName("msg")
    override val msg: String?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("user_index")
    val userIndex: Int
): Response