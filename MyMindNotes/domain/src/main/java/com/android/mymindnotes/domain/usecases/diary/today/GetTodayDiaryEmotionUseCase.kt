package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiaryEmotionUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun getEmotion(): Flow<String?> {
//        return repository.getEmotion()
//    }

    suspend operator fun invoke(): Flow<String?> = repository.getEmotion()

}