package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiarySituationUseCase @Inject constructor(
    private val repository: TraumaDiaryLocalRepository
) {

//    suspend fun getSituation(): Flow<String?> {
//        return repository.getSituation()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getSituation()
    }
}