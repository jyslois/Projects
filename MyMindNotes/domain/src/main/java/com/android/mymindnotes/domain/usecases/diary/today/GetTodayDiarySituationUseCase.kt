package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiarySituationUseCase @Inject constructor(
    private val repository: TodayDiaryRepository
) {
//    suspend fun getSituation(): Flow<String?> {
//        return repository.getSituation()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getSituation()
    }
}