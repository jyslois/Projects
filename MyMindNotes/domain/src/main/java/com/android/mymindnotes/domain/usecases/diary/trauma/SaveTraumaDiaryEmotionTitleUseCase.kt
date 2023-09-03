package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionTitleUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {
    suspend operator fun invoke(emotion: String?) = repository.saveEmotion(emotion)

}