package com.android.mymindnotes.core.model


import com.google.gson.annotations.SerializedName

data class DeleteDiaryResponse(
    @SerializedName("code")
    val code: Int
)