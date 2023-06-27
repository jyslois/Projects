package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {


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