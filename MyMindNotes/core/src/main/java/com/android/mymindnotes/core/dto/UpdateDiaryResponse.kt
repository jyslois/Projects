package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class UpdateDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val message: String
)