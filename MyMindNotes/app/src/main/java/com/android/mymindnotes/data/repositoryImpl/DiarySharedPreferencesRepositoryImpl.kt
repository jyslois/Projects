package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.DiarySharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.DiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiarySharedPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: DiarySharedPreferencesDataSource
): DiarySharedPreferencesRepository {

    // Save Methods
    override suspend fun saveEmotionColor(color: Int) {
        dataSource.saveEmotionColor(color)
    }

    override suspend fun saveEmotion(emotion: String?) {
        dataSource.saveEmotion(emotion)
    }

    override suspend fun saveEmotionText(emotionText: String?) {
        dataSource.saveEmotionText(emotionText)
    }

    // Get Methods
    override suspend fun getEmotion(): Flow<String?> = dataSource.getEmotion

    override suspend fun getEmotionText(): Flow<String?> = dataSource.getEmotionText

    // Clear Methods
    override suspend fun clearEmotionColorSharedPreferences() {
        dataSource.clearEmotionColorSharedPreferences()
    }

    override suspend fun clearEmotionSharedPreferences() {
        dataSource.clearEmotionSharedPreferences()
    }

    override suspend fun clearEmotionTextSharedPreferences() {
        dataSource.clearEmotionTextSharedPreferences()
    }

    override suspend fun clearSituationSharedPreferences() {
        dataSource.clearSituationSharedPreferences()
    }

    override suspend fun clearThoughtSharedPreferences() {
        dataSource.clearThoughtSharedPreferences()
    }

    override suspend fun clearReflectionSharedPreferences() {
        dataSource.clearReflectionSharedPreferences()
    }

    override suspend fun clearTypeSharedPreferences() {
        dataSource.clearTypeSharedPreferences()
    }
}