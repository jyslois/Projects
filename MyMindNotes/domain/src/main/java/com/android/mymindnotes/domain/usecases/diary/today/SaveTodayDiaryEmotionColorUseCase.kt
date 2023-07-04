package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiaryEmotionColorUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun saveEmotionColor(color: Int) {
//        repository.saveEmotionColor(color)
//    }

    suspend operator fun invoke(color: Int) = repository.saveEmotionColor(color)

}