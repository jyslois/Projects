package com.android.mymindnotes.data.repositoryInterfaces

import kotlinx.coroutines.flow.Flow

interface TraumaDiaryRepository {

    // 로컬
    // save methods
    suspend fun saveEmotionColor(color: Int)
    suspend fun saveEmotion(emotion: String?)
    suspend fun saveEmotionText(emotionText: String?)
    suspend fun saveSituation(situation: String)
    suspend fun saveThought(thought: String?)
    suspend fun saveReflection(reflection: String?)
    suspend fun saveType(type: String)
    suspend fun saveDate(date: String)
    suspend fun saveDay(day: String)

    // get methods
    suspend fun getEmotion(): Flow<String?>
    suspend fun getEmotionText(): Flow<String?>
    suspend fun getSituation(): Flow<String?>
    suspend fun getThought(): Flow<String?>
    suspend fun getReflection(): Flow<String?>

    // clear method
    suspend fun clearTraumaDiaryTempRecords()


    // 서버
    // 일기 저장
    suspend fun saveDiary(): Flow<Map<String, Object>>

}