package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface DiaryRepository {
    // Get DiaryList
    suspend fun getDiaryList(): Flow<Map<String, Object>>

    // 에러 메시지
    val error: SharedFlow<Boolean>

}