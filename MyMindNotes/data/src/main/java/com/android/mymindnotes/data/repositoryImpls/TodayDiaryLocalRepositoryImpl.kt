package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.dataSources.TodayDiaryLocalDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodayDiaryLocalRepositoryImpl @Inject constructor(
    private val dataSource: TodayDiaryLocalDataSourceInterface
): TodayDiaryLocalRepository {

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

    override suspend fun saveSituation(situation: String) {
        dataSource.saveSituation(situation)
    }

    override suspend fun saveThought(thought: String) {
        dataSource.saveThought(thought)
    }

    override suspend fun saveReflection(reflection: String?) {
        dataSource.saveReflection(reflection)
    }

    override suspend fun saveType(type: String) {
        dataSource.saveType(type)
    }

    override suspend fun saveDate(date: String) {
        dataSource.saveDate(date)
    }

    override suspend fun saveDay(day: String) {
        dataSource.saveDay(day)
    }


    // Get Methods
    override suspend fun getEmotion(): Flow<String?> = dataSource.getEmotion

    override suspend fun getEmotionText(): Flow<String?> = dataSource.getEmotionText

    override suspend fun getSituation(): Flow<String?> = dataSource.getSituation

    override suspend fun getThought(): Flow<String?> = dataSource.getThought

    override suspend fun getReflection(): Flow<String?> = dataSource.getReflection

    // Clear Methods

    override suspend fun clearTodayDiaryTempRecords() {
        dataSource.clearTodayDiaryTempRecords()
    }

}