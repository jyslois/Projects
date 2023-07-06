package com.android.mymindnotes.domain.usecases.diary.today

import javax.inject.Inject

class SaveTodayDiaryEmotionUseCase @Inject constructor(
    private val saveTodayDiaryEmotionTitleUseCase: SaveTodayDiaryEmotionTitleUseCase,
    private val saveTodayDiaryEmotionColorUseCase: SaveTodayDiaryEmotionColorUseCase,
    private val saveTodayDiaryEmotionTextUseCase: SaveTodayDiaryEmotionTextUseCase

) {

    suspend operator fun invoke(emotion: String, emotionColor: Int, emotionText: String?) {
        saveTodayDiaryEmotionTitleUseCase(emotion)
        saveTodayDiaryEmotionColorUseCase(emotionColor)
        saveTodayDiaryEmotionTextUseCase(emotionText)
    }
}