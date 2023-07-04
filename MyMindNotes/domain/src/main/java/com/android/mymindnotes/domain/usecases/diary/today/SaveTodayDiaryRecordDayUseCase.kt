package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiaryRecordDayUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun saveRecordDay(day: String) {
//        repository.saveDay(day)
//    }

    suspend operator fun invoke(day: String) {
        repository.saveDay(day)
    }
}