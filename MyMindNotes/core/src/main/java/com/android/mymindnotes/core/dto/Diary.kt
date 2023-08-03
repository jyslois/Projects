package com.android.mymindnotes.core.dto


import com.google.gson.annotations.SerializedName

data class Diary(
    @SerializedName("date")
    val date: String,
    @SerializedName("day")
    val day: String,
    @SerializedName("diary_number")
    val diaryNumber: Int,
    @SerializedName("emotion")
    val emotion: String,
    @SerializedName("emotionDescription")
    val emotionDescription: String,
    @SerializedName("reflection")
    val reflection: String,
    @SerializedName("situation")
    val situation: String,
    @SerializedName("thought")
    val thought: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("user_index")
    val userIndex: Int
)