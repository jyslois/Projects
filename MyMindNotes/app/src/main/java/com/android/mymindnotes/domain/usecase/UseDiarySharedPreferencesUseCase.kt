package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.DiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseDiarySharedPreferencesUseCase @Inject constructor(
    private val repository: DiarySharedPreferencesRepository
) {

    // Save Methods
    suspend fun saveEmotionColor(color: Int) {
        repository.saveEmotionColor(color)
    }

    suspend fun saveEmotion(emotion: String?) {
        repository.saveEmotion(emotion)
    }

    suspend fun saveEmotionText(emotionText: String?) {
        repository.saveEmotionText(emotionText)
    }

    // Get Methods
    suspend fun getEmotion(): Flow<String?> {
        return repository.getEmotion()
    }
    
    suspend fun getEmotionText(): Flow<String?> {
        return repository.getEmotionText()
    }

    // Clear Methods
    suspend fun clearEmotionColorSharedPreferences() {
        repository.clearEmotionColorSharedPreferences()
    }

    suspend fun clearEmotionSharedPreferences() {
        repository.clearEmotionSharedPreferences()
    }

    suspend fun clearEmotionTextSharedPreferences() {
        repository.clearEmotionTextSharedPreferences()
    }

    suspend fun clearSituationSharedPreferences() {
        repository.clearSituationSharedPreferences()
    }

    suspend fun clearThoughtSharedPreferences() {
        repository.clearThoughtSharedPreferences()
    }

    suspend fun clearReflectionSharedPreferences() {
        repository.clearReflectionSharedPreferences()
    }

    suspend fun clearTypeSharedPreferences() {
        repository.clearTypeSharedPreferences()
    }

}