package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.Flow

interface TraumaDiarySharedPreferencesRepository {

    // save methods
    suspend fun saveEmotionColor(color: Int)
    suspend fun saveEmotion(emotion: String?)
    suspend fun saveEmotionText(emotionText: String?)
    suspend fun saveSituation(situation: String)

    // get methods
    suspend fun getEmotion(): Flow<String?>
    suspend fun getEmotionText(): Flow<String?>
    suspend fun getSituation(): Flow<String?>

    // clear methods
    suspend fun clearEmotionColorSharedPreferences()
    suspend fun clearEmotionSharedPreferences()
    suspend fun clearEmotionTextSharedPreferences()
    suspend fun clearSituationSharedPreferences()
    suspend fun clearThoughtSharedPreferences()
    suspend fun clearReflectionSharedPreferences()
    suspend fun clearTypeSharedPreferences()

}