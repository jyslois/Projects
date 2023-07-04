package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiaryThoughtUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun saveThought(thought: String) {
//        repository.saveThought(thought)
//    }

    suspend operator fun invoke(thought: String) {
        repository.saveThought(thought)
    }
}