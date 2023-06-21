package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTodayDiaryReflectionUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {

//    suspend fun saveReflection(reflection: String?) {
//        repository.saveReflection(reflection)
//    }

    suspend operator fun invoke(reflection: String?) {
        repository.saveReflection(reflection)
    }
}