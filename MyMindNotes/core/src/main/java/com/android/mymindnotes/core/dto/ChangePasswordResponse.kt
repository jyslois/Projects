package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("code")
    override val code: Int? = 1,
    @SerializedName("msg")
    override val msg: String? = null
): Response