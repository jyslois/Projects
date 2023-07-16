package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryThoughtUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

    //    suspend fun saveThought(thought: String) {
//        repository.saveThought(thought)
//    }


    suspend operator fun invoke(thought: String) {
        repository.saveThought(thought)
    }
}