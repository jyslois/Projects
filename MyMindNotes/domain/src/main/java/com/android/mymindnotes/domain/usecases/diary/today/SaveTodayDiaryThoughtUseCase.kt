package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTodayDiaryThoughtUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {

//    suspend fun saveThought(thought: String) {
//        repository.saveThought(thought)
//    }

    suspend operator fun invoke(thought: String) {
        repository.saveThought(thought)
    }
}