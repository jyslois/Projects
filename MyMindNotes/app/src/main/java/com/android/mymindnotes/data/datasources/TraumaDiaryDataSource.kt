package com.android.mymindnotes.data.datasources

import com.android.mymindnotes.data.retrofit.api.diary.SaveDiaryApi
import com.android.mymindnotes.data.retrofit.model.diary.UserDiary
import com.android.mymindnotes.hilt.module.coroutine.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TraumaDiaryDataSource @Inject constructor(
    private val saveDiaryApi: SaveDiaryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val memberSharedPreferencesDataSource: MemberSharedPreferencesDataSource,
    private val diarySharedPreferencesDataSource: TraumaDiarySharedPreferencesDataSource
) {

    // (서버) 일기 저장하기
    val saveDiary: Flow<Map<String, Object>> = flow {
        var userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        val type = diarySharedPreferencesDataSource.getType.first()
        val date = diarySharedPreferencesDataSource.getDate.first()
        val day = diarySharedPreferencesDataSource.getDay.first()
        val situation = diarySharedPreferencesDataSource.getSituation.first()
        val thought = diarySharedPreferencesDataSource.getThought.first()
        val emotion = diarySharedPreferencesDataSource.getEmotion.first()
        val emotionText = diarySharedPreferencesDataSource.getEmotionText.first()
        val reflection = diarySharedPreferencesDataSource.getReflection.first()

        val userDiary = UserDiary(userIndex, type, date, day, situation, thought, emotion, emotionText, reflection)

        val result = saveDiaryApi.addDiary(userDiary)
        emit(result)

    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
        }

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()
}