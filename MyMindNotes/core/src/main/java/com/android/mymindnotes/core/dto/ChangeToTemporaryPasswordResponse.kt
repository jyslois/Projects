package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class ChangeToTemporaryPasswordResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String
)