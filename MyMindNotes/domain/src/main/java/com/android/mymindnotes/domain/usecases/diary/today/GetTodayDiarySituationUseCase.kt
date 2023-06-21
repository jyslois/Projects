package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiarySharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiarySituationUseCase @Inject constructor(
    private val repository: TodayDiarySharedPreferencesRepository
) {
//    suspend fun getSituation(): Flow<String?> {
//        return repository.getSituation()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getSituation()
    }
}