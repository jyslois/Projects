package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class UpdateDiaryResponse(
    @SerializedName("code")
    override val code: Int?,
    @SerializedName("msg")
    override val msg: String?
): Response