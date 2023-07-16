package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryRecordDayUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

//    suspend fun saveDay(day: String) {
//        repository.saveDay(day)
//    }

    suspend operator fun invoke(day: String) {
        repository.saveDay(day)
    }
}