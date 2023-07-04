package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionColorUseCase  @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun saveEmotionColor(color: Int) {
//        repository.saveEmotionColor(color)
//    }

    suspend operator fun invoke(color: Int) = repository.saveEmotionColor(color)

}