package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class JoinResponse(
    @SerializedName("code")
    override val code: Int? = 1,
    @SerializedName("msg")
    override val msg: String? = null,
    @SerializedName("user_index")
    val userIndex: Int
): Response