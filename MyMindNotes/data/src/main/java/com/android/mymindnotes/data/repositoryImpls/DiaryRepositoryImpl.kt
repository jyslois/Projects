package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.core.model.DeleteDiaryResponse
import com.android.mymindnotes.data.dataSources.DiaryRemoteDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.core.model.DiaryListResponse
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
): DiaryRepository {

    // Remote
    // Get Diary List
    override suspend fun getDiaryList(): Flow<DiaryListResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return diaryRemoteDataSource.getDiaryList(userIndex)
    }

    // Delete Diary
    override suspend fun deleteDiary(diaryNumber: Int): Flow<DeleteDiaryResponse> = diaryRemoteDataSource.deleteDiary(diaryNumber)

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