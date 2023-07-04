package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayDiaryReflectionUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

    suspend operator fun invoke(): Flow<String?> {
        return repository.getReflection()
    }
}