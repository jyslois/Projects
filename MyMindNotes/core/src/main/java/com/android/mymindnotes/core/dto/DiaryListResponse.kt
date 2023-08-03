package com.android.mymindnotes.core.dto

import com.google.gson.annotations.SerializedName

data class DiaryListResponse(
    @SerializedName("diaryList")
    val diaryList: ArrayList<Diary>
)