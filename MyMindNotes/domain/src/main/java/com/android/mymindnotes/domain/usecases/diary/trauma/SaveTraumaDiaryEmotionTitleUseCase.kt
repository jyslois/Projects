package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionTitleUseCase @Inject constructor(
    private val repository: TraumaDiaryRepository
) {

//    suspend fun saveEmotion(emotion: String?) {
//        repository.saveEmotion(emotion)
//    }

    suspend operator fun invoke(emotion: String?) = repository.saveEmotion(emotion)


}