package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.dataSources.DiaryDataSource
import com.android.mymindnotes.data.dataSources.MemberSharedPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryDataSource: DiaryDataSource,
    private val memberSharedPreferencesDataSource: MemberSharedPreferencesDataSource,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
): DiaryRepository {


    // Get Diary List
    override suspend fun getDiaryList(): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return diaryDataSource.getDiaryList(userIndex)
    }

    // Delete Diary
    override suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = diaryDataSource.deleteDiary(diaryNumber)

    // Update Diary
    override suspend fun updateDiary(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ): Flow<Map<String, Object>> = diaryDataSource.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection)


}