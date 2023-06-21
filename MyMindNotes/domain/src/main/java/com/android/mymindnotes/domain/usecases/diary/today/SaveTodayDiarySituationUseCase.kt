package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTodayDiarySituationUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {

//    suspend fun saveSituation(situation: String) {
//        repository.saveSituation(situation)
//    }

    suspend operator fun invoke(situation: String) {
        repository.saveSituation(situation)
    }
}