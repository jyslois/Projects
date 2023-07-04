package com.android.mymindnotes.data.repositoryInterfaces

import kotlinx.coroutines.flow.Flow

interface DiaryRemoteRepository {
    // Get DiaryList
    suspend fun getDiaryList(): Flow<Map<String, Object>>

    // delete Diary
    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>>

    // update Diary
    suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<Map<String, Object>>

}