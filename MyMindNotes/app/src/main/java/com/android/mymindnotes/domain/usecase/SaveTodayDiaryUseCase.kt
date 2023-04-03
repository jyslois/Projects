package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.TodayDiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveTodayDiaryUseCase @Inject constructor(
    private val repository: TodayDiaryRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // (서버) 일기 저장
    suspend fun saveDiary(): Flow<Map<String, Object>> {
        return repository.saveDiary()
    }

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            // 에러 감지
            repository.error.collect {
                _error.emit(it)
            }
        }
    }

}