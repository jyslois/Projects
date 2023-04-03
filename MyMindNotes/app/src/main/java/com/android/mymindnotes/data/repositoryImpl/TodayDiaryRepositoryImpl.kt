package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.TodayDiaryDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.TodayDiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodayDiaryRepositoryImpl @Inject constructor(
    private val diaryDataSource: TodayDiaryDataSource,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) : TodayDiaryRepository {

    // (서버) 일기 저장하기
    override suspend fun saveDiary(): Flow<Map<String, Object>> = diaryDataSource.saveDiary

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