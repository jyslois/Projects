package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveTraumaDiaryUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {
//    // (서버) 일기 저장
//    suspend fun saveDiary(): Flow<Map<String, Object>> {
//        return repository.saveDiary()
//    }

    suspend operator fun invoke(): Flow<Map<String, Object>> {
        return repository.saveDiary()
    }

}