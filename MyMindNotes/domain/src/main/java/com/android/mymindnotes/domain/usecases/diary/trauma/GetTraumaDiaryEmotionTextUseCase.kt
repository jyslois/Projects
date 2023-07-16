package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiaryEmotionTextUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

//    suspend fun getEmotionText(): Flow<String?> {
//        return repository.getEmotionText()
//    }

    suspend operator fun invoke(): Flow<String?> = repository.getEmotionText()
}