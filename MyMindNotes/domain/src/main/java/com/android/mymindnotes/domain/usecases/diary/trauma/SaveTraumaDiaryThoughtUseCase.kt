package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTraumaDiaryThoughtUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {

    //    suspend fun saveThought(thought: String) {
//        repository.saveThought(thought)
//    }


    suspend operator fun invoke(thought: String) {
        repository.saveThought(thought)
    }
}