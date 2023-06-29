package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiaryListUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

//    suspend fun getDiaryList(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()

    suspend operator fun invoke(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()

}