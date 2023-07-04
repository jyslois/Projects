package com.android.mymindnotes.data.repositoryInterfaces

import kotlinx.coroutines.flow.Flow

interface TraumaDiaryRemoteRepository {

    // 일기 저장
    suspend fun saveDiary(): Flow<Map<String, Object>>

}