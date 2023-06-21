package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTraumaDiarySituationUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {

//    suspend fun getSituation(): Flow<String?> {
//        return repository.getSituation()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getSituation()
    }
}