package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class SaveTraumaDiaryTypeUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun saveType(type: String) {
//        repository.saveType(type)
//    }

    suspend operator fun invoke(type: String) {
        repository.saveType(type)
    }
}