package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRemoteRepository
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.TodayDiaryRemoteDataSourceInterface
import com.android.mymindnotes.data.dataSources.TodayDiaryLocalDataSourceInterface
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TodayDiaryRemoteRepositoryImpl @Inject constructor(
    private val diaryDataSource: TodayDiaryRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface,
    private val diarySharedPreferencesDataSource: TodayDiaryLocalDataSourceInterface
): TodayDiaryRemoteRepository {

    // (서버) 일기 저장하기
    override suspend fun saveDiary(): Flow<Map<String, Object>> {
        var userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        val type = diarySharedPreferencesDataSource.getType.first()
        val date = diarySharedPreferencesDataSource.getDate.first()
        val day = diarySharedPreferencesDataSource.getDay.first()
        val situation = diarySharedPreferencesDataSource.getSituation.first()
        val thought = diarySharedPreferencesDataSource.getThought.first()
        val emotion = diarySharedPreferencesDataSource.getEmotion.first()
        val emotionText = diarySharedPreferencesDataSource.getEmotionText.first()
        val reflection = diarySharedPreferencesDataSource.getReflection.first()
        return diaryDataSource.saveDiary(userIndex, type, date, day, situation, thought, emotion, emotionText, reflection)
    }

}