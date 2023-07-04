package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class ClearTraumaDiaryTempRecordsUseCase@Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun clearTraumaDiaryTempRecords() {
//        repository.clearTraumaDiaryTempRecords()
//    }

    suspend operator fun invoke() {
        repository.clearTraumaDiaryTempRecords()
    }
}