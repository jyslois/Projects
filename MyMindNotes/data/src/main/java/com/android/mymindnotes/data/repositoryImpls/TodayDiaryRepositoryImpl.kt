package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.data.dataSources.MemberSharedPreferencesDataSource
import com.android.mymindnotes.data.dataSources.TodayDiaryDataSource
import com.android.mymindnotes.data.dataSources.TodayDiarySharedPreferencesDataSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TodayDiaryRepositoryImpl @Inject constructor(
    private val diaryDataSource: TodayDiaryDataSource,
    private val memberSharedPreferencesDataSource: MemberSharedPreferencesDataSource,
    private val diarySharedPreferencesDataSource: TodayDiarySharedPreferencesDataSource
): TodayDiaryRepository {

    // (서버) 일기 저장하기
    override suspend fun saveDiary(): Flow<Map<String, Object>> {
        var userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
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