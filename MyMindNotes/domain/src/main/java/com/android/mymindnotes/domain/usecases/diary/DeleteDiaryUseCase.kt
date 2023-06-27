package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

//    // Delete Diary
//    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = diaryRepository.deleteDiary(diaryNumber)

    suspend operator fun invoke(diaryNumber: Int): Flow<Map<String, Object>> = diaryRepository.deleteDiary(diaryNumber)

}