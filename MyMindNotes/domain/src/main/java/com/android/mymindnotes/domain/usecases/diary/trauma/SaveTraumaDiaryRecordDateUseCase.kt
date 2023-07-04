package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class SaveTraumaDiaryRecordDateUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun saveDate(date: String) {
//        repository.saveDate(date)
//    }

    suspend operator fun invoke(date: String) {
        repository.saveDate(date)
    }
}