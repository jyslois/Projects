package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.TodayDiaryLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.TodayDiaryRemoteDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TodayDiaryRepositoryImpl @Inject constructor(
    private val todayDiaryLocalDataSource: TodayDiaryLocalDataSourceInterface,
    private val todayDiaryRemoteDataSource: TodayDiaryRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
): TodayDiaryRepository {

    // 로컬
    // Save Methods
    override suspend fun saveEmotionColor(color: Int) {
        todayDiaryLocalDataSource.saveEmotionColor(color)
    }

    override suspend fun saveEmotion(emotion: String?) {
        todayDiaryLocalDataSource.saveEmotion(emotion)
    }

    override suspend fun saveEmotionText(emotionText: String?) {
        todayDiaryLocalDataSource.saveEmotionText(emotionText)
    }

    override suspend fun saveSituation(situation: String) {
        todayDiaryLocalDataSource.saveSituation(situation)
    }

    override suspend fun saveThought(thought: String) {
        todayDiaryLocalDataSource.saveThought(thought)
    }

    override suspend fun saveReflection(reflection: String?) {
        todayDiaryLocalDataSource.saveReflection(reflection)
    }

    override suspend fun saveType(type: String) {
        todayDiaryLocalDataSource.saveType(type)
    }

    override suspend fun saveDate(date: String) {
        todayDiaryLocalDataSource.saveDate(date)
    }

    override suspend fun saveDay(day: String) {
        todayDiaryLocalDataSource.saveDay(day)
    }


    // Get Methods
    override suspend fun getEmotion(): Flow<String?> = todayDiaryLocalDataSource.getEmotion

    override suspend fun getEmotionText(): Flow<String?> = todayDiaryLocalDataSource.getEmotionText

    override suspend fun getSituation(): Flow<String?> = todayDiaryLocalDataSource.getSituation

    override suspend fun getThought(): Flow<String?> = todayDiaryLocalDataSource.getThought

    override suspend fun getReflection(): Flow<String?> = todayDiaryLocalDataSource.getReflection

    // Clear Methods

    override suspend fun clearTodayDiaryTempRecords() {
        todayDiaryLocalDataSource.clearTodayDiaryTempRecords()
    }

    // 서버
    // 일기 저장하기
    override suspend fun saveDiary(): Flow<Map<String, Object>> {
        var userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        val type = todayDiaryLocalDataSource.getType.first()
        val date = todayDiaryLocalDataSource.getDate.first()
        val day = todayDiaryLocalDataSource.getDay.first()
        val situation = todayDiaryLocalDataSource.getSituation.first()
        val thought = todayDiaryLocalDataSource.getThought.first()
        val emotion = todayDiaryLocalDataSource.getEmotion.first()
        val emotionText = todayDiaryLocalDataSource.getEmotionText.first()
        val reflection = todayDiaryLocalDataSource.getReflection.first()
        return todayDiaryRemoteDataSource.saveDiary(userIndex, type, date, day, situation, thought, emotion, emotionText, reflection)
    }

}