package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiaryEmotionPartsUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {

    suspend fun getEmotion(): Flow<String?> {
        return repository.getEmotion()
    }

    suspend fun getEmotionText(): Flow<String?> {
        return repository.getEmotionText()
    }

}