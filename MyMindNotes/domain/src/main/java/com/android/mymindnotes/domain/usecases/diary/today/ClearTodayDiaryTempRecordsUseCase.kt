package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import javax.inject.Inject

class ClearTodayDiaryTempRecordsUseCase @Inject constructor(
    private val repository: TodayDiaryRepository
) {
    suspend operator fun invoke() {
        repository.clearTodayDiaryTempRecords()
    }

}