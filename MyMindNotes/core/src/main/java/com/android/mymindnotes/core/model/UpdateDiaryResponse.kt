package com.android.mymindnotes.core.model


import com.google.gson.annotations.SerializedName

data class UpdateDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val message: String
)