package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class SaveTraumaDiaryReflectionUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun saveReflection(reflection: String?) {
//        repository.saveReflection(reflection)
//    }

    suspend operator fun invoke(reflection: String?) {
        repository.saveReflection(reflection)
    }

}