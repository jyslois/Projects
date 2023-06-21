package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTodayDiaryEmotionPartsUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
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

    suspend fun saveSituation(situation: String) {
        repository.saveSituation(situation)
    }
}