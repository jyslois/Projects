package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTodayDiaryRecordDateUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {

//    suspend fun saveRecordDate(date: String) {
//        repository.saveDate(date)
//    }

    suspend operator fun invoke(date: String) {
        repository.saveDate(date)
    }
}