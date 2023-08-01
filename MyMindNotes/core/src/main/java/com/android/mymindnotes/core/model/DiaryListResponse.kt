package com.android.mymindnotes.core.model

import com.google.gson.annotations.SerializedName

data class DiaryListResponse(
    @SerializedName("diaryList")
    val diaryList: ArrayList<Diary>
)