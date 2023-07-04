package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiaryEmotionUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun getEmotion(): Flow<String?> {
//        return repository.getEmotion()
//    }

    suspend operator fun invoke(): Flow<String?> = repository.getEmotion()


}