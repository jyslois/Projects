package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.core.dto.DiaryEdit
import com.android.mymindnotes.data.dataSources.DiaryRemoteDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
): DiaryRepository {

    // Remote
    // Get Diary List
    override suspend fun getDiaryList(): Flow<GetDiaryListResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return diaryRemoteDataSource.getDiaryList(userIndex)
    }

    // Delete Diary
    override suspend fun deleteDiary(diaryNumber: Int): Flow<DeleteDiaryResponse> = diaryRemoteDataSource.deleteDiary(diaryNumber)

    // Update Diary
    override suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<UpdateDiaryResponse> {
        val diary = DiaryEdit(situation, thought, emotion, emotionDescription, reflection)
        return diaryRemoteDataSource.updateDiary(diaryNumber, diary)
    }

}