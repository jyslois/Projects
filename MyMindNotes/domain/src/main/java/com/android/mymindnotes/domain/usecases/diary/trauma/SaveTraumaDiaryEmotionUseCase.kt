package com.android.mymindnotes.domain.usecases.diary.trauma

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiarySharedPreferencesRepository
import javax.inject.Inject

class SaveTraumaDiaryEmotionUseCase @Inject constructor(
    private val repository: TraumaDiarySharedPreferencesRepository
) {

//    suspend fun saveEmotion(emotion: String?) {
//        repository.saveEmotion(emotion)
//    }

    suspend operator fun invoke(emotion: String?) = repository.saveEmotion(emotion)


}