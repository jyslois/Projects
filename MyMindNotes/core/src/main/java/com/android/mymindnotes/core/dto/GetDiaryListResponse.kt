package com.android.mymindnotes.core.dto

import com.google.gson.annotations.SerializedName

data class GetDiaryListResponse(
    @SerializedName("code")
    override val code: Int? = 1,
    @SerializedName("msg")
    override val msg: String? = null,
    @SerializedName("diaryList")
    val diaryList: ArrayList<Diary>
): Response