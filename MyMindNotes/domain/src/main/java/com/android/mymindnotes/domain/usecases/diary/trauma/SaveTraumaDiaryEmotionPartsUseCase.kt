package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionPartsUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {

    suspend fun saveEmotionColor(color: Int) {
        repository.saveEmotionColor(color)
    }

    suspend fun saveEmotion(emotion: String?) {
        repository.saveEmotion(emotion)
    }

    suspend fun saveEmotionText(emotionText: String?) {
        repository.saveEmotionText(emotionText)
    }

}