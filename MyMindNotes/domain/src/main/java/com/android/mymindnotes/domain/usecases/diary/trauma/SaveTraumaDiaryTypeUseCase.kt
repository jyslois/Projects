package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryTypeUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

//    suspend fun saveType(type: String) {
//        repository.saveType(type)
//    }

    suspend operator fun invoke(type: String) {
        repository.saveType(type)
    }
}