package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import javax.inject.Inject

class SaveTodayDiaryEmotionColorUseCase @Inject constructor(
    private val repository: TodayDiaryRepository
) {

//    suspend fun saveEmotionColor(color: Int) {
//        repository.saveEmotionColor(color)
//    }

    suspend operator fun invoke(color: Int) = repository.saveEmotionColor(color)

}