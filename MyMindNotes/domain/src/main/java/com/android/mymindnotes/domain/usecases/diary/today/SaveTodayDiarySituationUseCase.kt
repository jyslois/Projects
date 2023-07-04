package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiarySituationUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun saveSituation(situation: String) {
//        repository.saveSituation(situation)
//    }

    suspend operator fun invoke(situation: String) {
        repository.saveSituation(situation)
    }
}