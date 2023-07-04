package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class SaveTraumaDiaryRecordDayUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun saveDay(day: String) {
//        repository.saveDay(day)
//    }

    suspend operator fun invoke(day: String) {
        repository.saveDay(day)
    }
}