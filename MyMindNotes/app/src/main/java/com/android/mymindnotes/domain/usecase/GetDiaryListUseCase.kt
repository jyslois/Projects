package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.DiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
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
    private val _getDiaryListError = MutableSharedFlow<Boolean>()
    val getDiaryListError = _getDiaryListError.asSharedFlow()

    suspend fun getDiaryList(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()


    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // error collect & emit
                diaryRepository.getDiaryListError.collect {
                    _getDiaryListError.emit(it)
                }
            }
        }
    }

}