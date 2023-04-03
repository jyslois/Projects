package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface TodayDiaryRepository {

    // 일기 저장
    suspend fun saveDiary(): Flow<Map<String, Object>>

    // 에러 메시지
    val error: SharedFlow<Boolean>

}