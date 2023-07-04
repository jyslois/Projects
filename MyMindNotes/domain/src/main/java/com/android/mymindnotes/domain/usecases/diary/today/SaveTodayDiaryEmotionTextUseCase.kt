package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiaryEmotionTextUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {

//    suspend fun saveEmotionText(emotionText: String?) {
//        repository.saveEmotionText(emotionText)
//    }

    suspend operator fun invoke(emotionText: String?) = repository.saveEmotionText(emotionText)

}