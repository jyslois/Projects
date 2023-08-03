package com.android.mymindnotes.data.repositoryInterfaces

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.core.dto.DiaryListResponse
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    // Get DiaryListResponse
    suspend fun getDiaryList(): Flow<DiaryListResponse>

    // delete Diary
    suspend fun deleteDiary(diaryNumber: Int): Flow<DeleteDiaryResponse>

    // update Diary
    suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<UpdateDiaryResponse>

}