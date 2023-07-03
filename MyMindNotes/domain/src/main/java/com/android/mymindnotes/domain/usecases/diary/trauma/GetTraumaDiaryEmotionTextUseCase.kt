package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiaryEmotionTextUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {

//    suspend fun getEmotionText(): Flow<String?> {
//        return repository.getEmotionText()
//    }

    suspend operator fun invoke(): Flow<String?> = repository.getEmotionText()
}