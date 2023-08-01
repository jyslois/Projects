package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi
import com.android.mymindnotes.data.retrofit.api.diary.UpdateDiaryApi
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import com.android.mymindnotes.data.retrofit.model.diary.DiaryEdit
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import com.android.mymindnotes.core.model.DiaryListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DiaryRemoteDataSource @Inject constructor(
    private val getDiaryListApi: GetDiaryListApi,
    private val deleteDiaryApi: DeleteDiaryApi,
    private val updateDiaryApi: UpdateDiaryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): DiaryRemoteDataSourceInterface {

    // 일기 리스트 가져오기
    override suspend fun getDiaryList(userIndex: Int): Flow<DiaryListResponse> = flow {
        val diaryList = getDiaryListApi.getAllDiary(userIndex)
        emit(diaryList)
    }.flowOn(ioDispatcher)


    // 일기 삭제하기
    override suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = flow {
        val result = deleteDiaryApi.deleteDiary(diaryNumber)
        emit(result)
    }.flowOn(ioDispatcher)

    // 일기 수정하기
    override suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<Map<String, Object>> = flow {
        val diary = DiaryEdit(situation, thought, emotion, emotionDescription, reflection)
        val result = updateDiaryApi.updateDiary(diaryNumber, diary)
        emit(result)
    }.flowOn(ioDispatcher)

}

interface DiaryRemoteDataSourceInterface {
    suspend fun getDiaryList(userIndex: Int): Flow<DiaryListResponse>
    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>>
    suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<Map<String, Object>>

}