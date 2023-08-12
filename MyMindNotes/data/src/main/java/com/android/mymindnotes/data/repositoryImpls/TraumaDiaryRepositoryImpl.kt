package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.core.dto.SaveDiaryResponse
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.TraumaDiaryLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.TraumaDiaryRemoteDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraumaDiaryRepositoryImpl @Inject constructor(
    private val traumaDiaryLocalDataSource: TraumaDiaryLocalDataSourceInterface,
    private val traumaDiaryRemoteDataSource: TraumaDiaryRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
) : TraumaDiaryRepository {

    // 로컬
    // Save Methods
    override suspend fun saveEmotionColor(color: Int) {
        traumaDiaryLocalDataSource.saveEmotionColor(color)
    }

    override suspend fun saveEmotion(emotion: String?) {
        traumaDiaryLocalDataSource.saveEmotion(emotion)
    }

    override suspend fun saveEmotionText(emotionText: String?) {
        traumaDiaryLocalDataSource.saveEmotionText(emotionText)
    }

    override suspend fun saveSituation(situation: String) {
        traumaDiaryLocalDataSource.saveSituation(situation)
    }

    override suspend fun saveThought(thought: String?) {
        traumaDiaryLocalDataSource.saveThought(thought)
    }

    override suspend fun saveReflection(reflection: String?) {
        traumaDiaryLocalDataSource.saveReflection(reflection)
    }

    override suspend fun saveType(type: String) {
        traumaDiaryLocalDataSource.saveType(type)
    }

    override suspend fun saveDate(date: String) {
        traumaDiaryLocalDataSource.saveDate(date)
    }

    override suspend fun saveDay(day: String) {
        traumaDiaryLocalDataSource.saveDay(day)
    }

    // Get Methods
    override suspend fun getEmotion(): Flow<String?> = traumaDiaryLocalDataSource.getEmotion

    override suspend fun getEmotionText(): Flow<String?> = traumaDiaryLocalDataSource.getEmotionText

    override suspend fun getSituation(): Flow<String?> = traumaDiaryLocalDataSource.getSituation

    override suspend fun getThought(): Flow<String?> = traumaDiaryLocalDataSource.getThought

    override suspend fun getReflection(): Flow<String?> = traumaDiaryLocalDataSource.getReflection


    // Clear Methods
    override suspend fun clearTraumaDiaryTempRecords() {
        traumaDiaryLocalDataSource.clearTraumaDiaryTempRecords()
    }


    // 서버
    // 일기 저장하기
    override suspend fun saveDiary(): Flow<SaveDiaryResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        val type = traumaDiaryLocalDataSource.getType.first()
        val date = traumaDiaryLocalDataSource.getDate.first()
        val day = traumaDiaryLocalDataSource.getDay.first()
        val situation = traumaDiaryLocalDataSource.getSituation.first()
        val thought = traumaDiaryLocalDataSource.getThought.first()
        val emotion = traumaDiaryLocalDataSource.getEmotion.first()
        val emotionText = traumaDiaryLocalDataSource.getEmotionText.first()
        val reflection = traumaDiaryLocalDataSource.getReflection.first()
        return traumaDiaryRemoteDataSource.saveDiary(userIndex, type, date, day, situation, thought, emotion, emotionText, reflection)
    }

}