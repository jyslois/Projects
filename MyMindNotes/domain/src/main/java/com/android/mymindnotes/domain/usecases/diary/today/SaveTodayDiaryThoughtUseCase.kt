package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import javax.inject.Inject

class SaveTodayDiaryThoughtUseCase @Inject constructor(
    private val repository: TodayDiaryRepository
) {

//    suspend fun saveThought(thought: String) {
//        repository.saveThought(thought)
//    }

    suspend operator fun invoke(thought: String) {
        repository.saveThought(thought)
    }
}