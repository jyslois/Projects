package com.android.mymindnotes.data.repositoryInterfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface TraumaDiaryRepository {

    // 일기 저장
    suspend fun saveDiary(): Flow<Map<String, Object>>

    // 에러 메시지
    val error: SharedFlow<Boolean>

}