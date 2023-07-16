package com.android.mymindnotes.domain.usecases.diary.trauma

import javax.inject.Inject

class SaveTraumaDiaryEmotionUseCase @Inject constructor(
    private val saveTraumaDiaryEmotionTitleUseCase: SaveTraumaDiaryEmotionTitleUseCase,
    private val saveTraumaDiaryEmotionColorUseCase: SaveTraumaDiaryEmotionColorUseCase,
    private val saveTraumaDiaryEmotionTextUseCase: SaveTraumaDiaryEmotionTextUseCase
) {
    suspend operator fun invoke(emotion: String?, emotionColor: Int, emotionText: String?) {
        saveTraumaDiaryEmotionTitleUseCase(emotion)
        saveTraumaDiaryEmotionColorUseCase(emotionColor)
        saveTraumaDiaryEmotionTextUseCase(emotionText)
    }
}