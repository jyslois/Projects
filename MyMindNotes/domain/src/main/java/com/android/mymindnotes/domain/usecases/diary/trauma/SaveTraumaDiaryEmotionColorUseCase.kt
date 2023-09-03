package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionColorUseCase  @Inject constructor(
    private val repository: TraumaDiaryRepository
) {
    suspend operator fun invoke(color: Int) = repository.saveEmotionColor(color)

}