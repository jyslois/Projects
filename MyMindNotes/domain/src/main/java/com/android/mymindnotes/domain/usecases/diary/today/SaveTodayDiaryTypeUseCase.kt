package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiaryTypeUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun saveType(type: String) {
//        repository.saveType(type)
//    }

    suspend operator fun invoke(type: String) {
        repository.saveType(type)
    }
}