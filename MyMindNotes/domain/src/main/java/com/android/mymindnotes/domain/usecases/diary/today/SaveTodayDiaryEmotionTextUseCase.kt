package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import javax.inject.Inject

class SaveTodayDiaryEmotionTextUseCase @Inject constructor(
    private val repository: TodayDiaryRepository
) {

//    suspend fun saveEmotionText(emotionText: String?) {
//        repository.saveEmotionText(emotionText)
//    }

    suspend operator fun invoke(emotionText: String?) = repository.saveEmotionText(emotionText)

}