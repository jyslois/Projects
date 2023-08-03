package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class ChangeNicknameResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String
)