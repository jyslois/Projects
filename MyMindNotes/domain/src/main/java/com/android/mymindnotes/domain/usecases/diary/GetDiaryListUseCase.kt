package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetDiaryListUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {
    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

//    suspend fun getDiaryList(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()

    suspend operator fun invoke(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()


    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // error collect & emit
                diaryRepository.getDiaryListError.collect {
                    _error.emit(it)
                }
            }
        }
    }

}