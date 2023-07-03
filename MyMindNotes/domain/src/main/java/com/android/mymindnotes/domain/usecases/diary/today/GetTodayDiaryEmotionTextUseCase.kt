package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiaryEmotionTextUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {
//    suspend fun getEmotionText(): Flow<String?> {
//        return repository.getEmotionText()
//    }

    suspend operator fun invoke(): Flow<String?> = repository.getEmotionText()
}