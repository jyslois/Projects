package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiaryThoughtUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun getThought(): Flow<String?> {
//        return repository.getThought()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getThought()
    }
}