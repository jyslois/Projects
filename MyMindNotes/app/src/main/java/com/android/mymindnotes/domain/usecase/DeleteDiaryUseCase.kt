package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.DiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {
    // 에러 메시지
    private val _deleteDiaryError = MutableSharedFlow<Boolean>()
    val deleteDiaryError = _deleteDiaryError.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                diaryRepository.deleteDiaryError.collect {
                    _deleteDiaryError.emit(it)
                }
            }
        }
    }

    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = diaryRepository.deleteDiary(diaryNumber)

}