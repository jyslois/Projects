package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface DiaryRepository {
    // Get DiaryList
    suspend fun getDiaryList(): Flow<Map<String, Object>>

    // delete Diary
    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>>

    // update Diary
    suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<Map<String, Object>>

    // 에러 메시지
    val getDiaryListError: SharedFlow<Boolean>
    val deleteDiaryError: SharedFlow<Boolean>
    val updateDiaryError: SharedFlow<Boolean>

}