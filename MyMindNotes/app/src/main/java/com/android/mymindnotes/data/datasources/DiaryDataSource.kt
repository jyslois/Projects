package com.android.mymindnotes.data.datasources

import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import com.android.mymindnotes.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DiaryDataSource @Inject constructor(
    private val getDiaryListApi: GetDiaryListApi,
    private val deleteDiaryApi: DeleteDiaryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    // 에러
    private val _getDiaryListError = MutableSharedFlow<Boolean>()
    val getDiaryListError = _getDiaryListError.asSharedFlow()

    private val _deleteDiaryError = MutableSharedFlow<Boolean>()
    val deleteDiaryError = _deleteDiaryError.asSharedFlow()

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

}