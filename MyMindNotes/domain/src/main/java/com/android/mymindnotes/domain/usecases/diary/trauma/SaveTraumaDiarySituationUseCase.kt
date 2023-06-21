package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTraumaDiarySituationUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {
//    suspend fun saveSituation(situation: String) {
//        repository.saveSituation(situation)
//    }

    suspend operator fun invoke(situation: String) {
        repository.saveSituation(situation)
    }
}