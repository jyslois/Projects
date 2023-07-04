package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.dataSources.DiaryDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRemoteRepository
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DiaryRemoteRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
): DiaryRemoteRepository {


    // Get Diary List
    override suspend fun getDiaryList(): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        return diaryRemoteDataSource.getDiaryList(userIndex)
    }

    // Delete Diary
    override suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = diaryRemoteDataSource.deleteDiary(diaryNumber)

    // Update Diary
    override suspend fun updateDiary(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ): Flow<Map<String, Object>> = diaryRemoteDataSource.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection)


}