package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi
import com.android.mymindnotes.data.retrofit.api.diary.UpdateDiaryApi
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import com.android.mymindnotes.core.dto.DiaryEdit
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
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
    override suspend fun getDiaryList(userIndex: Int): Flow<GetDiaryListResponse> = flow {
        val diaryList = getDiaryListApi.getAllDiary(userIndex)
        emit(diaryList)
    }.flowOn(ioDispatcher)


    // 일기 삭제하기
    override suspend fun deleteDiary(diaryNumber: Int): Flow<DeleteDiaryResponse> = flow {
        val result = deleteDiaryApi.deleteDiary(diaryNumber)
        emit(result)
    }.flowOn(ioDispatcher)

    // 일기 수정하기
    override suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<UpdateDiaryResponse> = flow {
        val diary = DiaryEdit(situation, thought, emotion, emotionDescription, reflection)
        val result = updateDiaryApi.updateDiary(diaryNumber, diary)
        emit(result)
    }.flowOn(ioDispatcher)

}

interface DiaryRemoteDataSourceInterface {
    suspend fun getDiaryList(userIndex: Int): Flow<GetDiaryListResponse>
    suspend fun deleteDiary(diaryNumber: Int): Flow<DeleteDiaryResponse>
    suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<UpdateDiaryResponse>
}