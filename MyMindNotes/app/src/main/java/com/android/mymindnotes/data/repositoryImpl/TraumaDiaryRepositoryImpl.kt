package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.MemberSharedPreferencesDataSource
import com.android.mymindnotes.data.datasources.TraumaDiaryDataSource
import com.android.mymindnotes.data.datasources.TraumaDiarySharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.TraumaDiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TraumaDiaryRepositoryImpl @Inject constructor(
    private val diaryDataSource: TraumaDiaryDataSource,
    private val memberSharedPreferencesDataSource: MemberSharedPreferencesDataSource,
    private val diarySharedPreferencesDataSource: TraumaDiarySharedPreferencesDataSource,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
): TraumaDiaryRepository {
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

    // 에러
    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    override val error: SharedFlow<Boolean> = _error.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            // 에러 메시지 collect & emit
            diaryDataSource.error.collect {
                _error.emit(it)
            }
        }
    }
}