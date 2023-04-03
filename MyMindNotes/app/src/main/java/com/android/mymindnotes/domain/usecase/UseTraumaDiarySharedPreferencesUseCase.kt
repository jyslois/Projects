package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.TraumaDiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseTraumaDiarySharedPreferencesUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
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

    suspend fun saveSituation(situation: String) {
        repository.saveSituation(situation)
    }

    suspend fun saveThought(thought: String) {
        repository.saveThought(thought)
    }

    suspend fun saveReflection(reflection: String) {
        repository.saveReflection(reflection)
    }

    suspend fun saveType(type: String) {
        repository.saveType(type)
    }

    suspend fun saveDate(date: String) {
        repository.saveDate(date)
    }

    suspend fun saveDay(day: String) {
        repository.saveDay(day)
    }

    // Get Methods
    suspend fun getEmotion(): Flow<String?> {
        return repository.getEmotion()
    }

    suspend fun getEmotionText(): Flow<String?> {
        return repository.getEmotionText()
    }

    suspend fun getSituation(): Flow<String?> {
        return repository.getSituation()
    }

    suspend fun getThought(): Flow<String?> {
        return repository.getThought()
    }

    suspend fun getReflection(): Flow<String?> {
        return repository.getReflection()
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

    suspend fun clearDateSharedPreferences() {
        repository.clearDateSharedPreferences()
    }

    suspend fun clearDaySharedPreferences() {
        repository.clearDaySharedPreferences()
    }

}