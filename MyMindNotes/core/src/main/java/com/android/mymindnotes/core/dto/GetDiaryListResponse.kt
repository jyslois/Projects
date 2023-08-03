package com.android.mymindnotes.core.dto

import com.google.gson.annotations.SerializedName

data class GetDiaryListResponse(
    @SerializedName("diaryList")
    val diaryList: ArrayList<Diary>
)