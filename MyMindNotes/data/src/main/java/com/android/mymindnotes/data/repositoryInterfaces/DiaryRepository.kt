package com.android.mymindnotes.data.repositoryInterfaces

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.core.dto.DiaryEdit
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    // Get GetDiaryListResponse
    suspend fun getDiaryList(): Flow<GetDiaryListResponse>

    // delete Diary
    suspend fun deleteDiary(diaryNumber: Int): Flow<DeleteDiaryResponse>

    // update Diary
    suspend fun updateDiary(diaryNumber: Int, diary: DiaryEdit): Flow<UpdateDiaryResponse>

}