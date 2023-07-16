package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionTextUseCase  @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

//    suspend fun saveEmotionText(emotionText: String?) {
//        repository.saveEmotionText(emotionText)
//    }

    suspend operator fun invoke(emotionText: String?) = repository.saveEmotionText(emotionText)
}