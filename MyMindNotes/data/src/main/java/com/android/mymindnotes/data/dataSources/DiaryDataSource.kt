package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi
import com.android.mymindnotes.data.retrofit.api.diary.UpdateDiaryApi
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import com.android.mymindnotes.data.retrofit.model.diary.DiaryEdit
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DiaryDataSource @Inject constructor(
    private val getDiaryListApi: GetDiaryListApi,
    private val deleteDiaryApi: DeleteDiaryApi,
    private val updateDiaryApi: UpdateDiaryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    // 에러
    private val _getDiaryListError = MutableSharedFlow<Boolean>()
    val getDiaryListError = _getDiaryListError.asSharedFlow()

    private val _deleteDiaryError = MutableSharedFlow<Boolean>()
    val deleteDiaryError = _deleteDiaryError.asSharedFlow()

    private val _updateDiaryError = MutableSharedFlow<Boolean>()
    val updateDiaryError = _updateDiaryError.asSharedFlow()

    // 일기 리스트 가져오기
    suspend fun getDiaryList(userIndex: Int): Flow<Map<String, Object>> = flow {
        val diaryList = getDiaryListApi.getAllDiary(userIndex)
        emit(diaryList)
    }.flowOn(ioDispatcher)
        .catch {
            _getDiaryListError.emit(true)
            _getDiaryListError.emit(false)
        }

    // 일기 삭제하기
    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = flow {
        val result = deleteDiaryApi.deleteDiary(diaryNumber)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _deleteDiaryError.emit(true)
            _deleteDiaryError.emit(false)
        }

    // 일기 수정하기
    suspend fun updateDiary(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?): Flow<Map<String, Object>> = flow {
        val diary = DiaryEdit(situation, thought, emotion, emotionDescription, reflection)
        val result = updateDiaryApi.updateDiary(diaryNumber, diary)
        emit(result)
    }.flowOn(ioDispatcher)

}