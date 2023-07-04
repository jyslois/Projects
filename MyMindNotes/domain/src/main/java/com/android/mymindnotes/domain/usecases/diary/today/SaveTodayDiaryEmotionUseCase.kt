package com.android.mymindnotes.domain.usecases.diary.today

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import javax.inject.Inject

class SaveTodayDiaryEmotionUseCase @Inject constructor(
    private val repository: TodayDiaryLocalRepository
) {
//    suspend fun saveEmotion(emotion: String?) {
//        repository.saveEmotion(emotion)
//    }

    suspend operator fun invoke(emotion: String?) = repository.saveEmotion(emotion)


}