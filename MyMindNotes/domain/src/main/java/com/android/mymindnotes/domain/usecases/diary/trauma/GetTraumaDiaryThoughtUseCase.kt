package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiaryThoughtUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

//    suspend fun getThought(): Flow<String?> {
//        return repository.getThought()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getThought()
    }

}