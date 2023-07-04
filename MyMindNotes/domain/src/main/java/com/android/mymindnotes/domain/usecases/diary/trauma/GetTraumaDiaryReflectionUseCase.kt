package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiaryReflectionUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun getReflection(): Flow<String?> {
//        return repository.getReflection()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getReflection()
    }
}