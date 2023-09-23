package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiaryEmotionUseCase @Inject constructor(
    private val repository: TodayDiaryRepository
) {
    suspend operator fun invoke(): Flow<String?> = repository.getEmotion()

}