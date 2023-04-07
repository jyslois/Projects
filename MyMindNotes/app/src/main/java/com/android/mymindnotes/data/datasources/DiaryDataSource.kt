package com.android.mymindnotes.data.datasources

import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import com.android.mymindnotes.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DiaryDataSource @Inject constructor(
    private val getDiaryListApi: GetDiaryListApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    // 일기 리스트 가져오기
    suspend fun getDiaryList(userIndex: Int): Flow<Map<String, Object>> = flow {
        val diaryList = getDiaryListApi.getAllDiary(userIndex)
        emit(diaryList)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
        }

}