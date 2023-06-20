package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // 에러
    private val _updateDiaryError = MutableSharedFlow<Boolean>()
    val updateDiaryError = _updateDiaryError.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // error collect & emit
                diaryRepository.updateDiaryError.collect {
                    _updateDiaryError.emit(it)
                }
            }
        }
    }

//    // Update Diary
//    suspend fun updateDiary(
//        diaryNumber: Int,
//        situation: String,
//        thought: String,
//        emotion: String,
//        emotionDescription: String?,
//        reflection: String?
//    ): Flow<Map<String, Object>> = diaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

    suspend operator fun invoke(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ): Flow<Map<String, Object>> = diaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

}