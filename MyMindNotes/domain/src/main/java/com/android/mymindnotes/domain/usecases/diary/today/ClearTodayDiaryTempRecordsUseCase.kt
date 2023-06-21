package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import javax.inject.Inject

class ClearTodayDiaryTempRecordsUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {

//    // Clear Method
//    suspend fun clearTodayDiaryTempRecords() {
//        repository.clearTodayDiaryTempRecords()
//    }

    suspend operator fun invoke() {
        repository.clearTodayDiaryTempRecords()
    }
}