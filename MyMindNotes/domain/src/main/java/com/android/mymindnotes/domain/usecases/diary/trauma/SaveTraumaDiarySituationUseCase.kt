package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class SaveTraumaDiarySituationUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {
//    suspend fun saveSituation(situation: String) {
//        repository.saveSituation(situation)
//    }

    suspend operator fun invoke(situation: String) {
        repository.saveSituation(situation)
    }
}