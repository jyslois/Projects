package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecase.UseTraumaDiarySharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryEmotionViewModel @Inject constructor(
    private val useCase: UseTraumaDiarySharedPreferencesUseCase
): ViewModel() {

    // 버튼 클릭
    // 감정 설명서 버튼 클릭 감지
    private val _recordEmotionHelpButton = MutableSharedFlow<Boolean>()
    val recordEmotionHelpButton = _recordEmotionHelpButton.asSharedFlow()

    // 감정 설명서 버튼 클릭
    suspend fun clickRecordEmotionHelpButton() {
        _recordEmotionHelpButton.emit(true)
    }

    // 팁 버튼 클릭 감지
    private val _recordEmotionTips = MutableSharedFlow<Boolean>()
    val recordEmotionTips = _recordEmotionTips.asSharedFlow()

    // 팁 버튼 클릭
    suspend fun clickRecordEmotionTips() {
        _recordEmotionTips.emit(true)
    }

    // 다음 버튼 클릭 감지
    private val _recordNextButton = MutableSharedFlow<Boolean>()
    val recordNextButton = _recordNextButton.asSharedFlow()

    // 다음 버튼 클릭
    suspend fun clickRecordNextButton() {
        _recordNextButton.emit(true)
    }

    // 이전 버튼 클릭 감지
    private val _recordPreviousButton = MutableSharedFlow<Boolean>()
    val recordPreviousButton = _recordPreviousButton.asSharedFlow()

    // 이전 버튼 클릭
    suspend fun clickRecordPreviousButton() {
        _recordPreviousButton.emit(true)
    }


    // Save Methods
    suspend fun saveEmotionColor(color: Int) {
        useCase.saveEmotionColor(color)
    }

    suspend fun saveEmotion(emotion: String?) {
        useCase.saveEmotion(emotion)
    }

    suspend fun saveEmotionText(emotionText: String?) {
        useCase.saveEmotionText(emotionText)
    }

    // Get Methods

    // Emotion Result Flow
    private val _emotion = MutableSharedFlow<String?>()
    val emotion = _emotion.asSharedFlow()

    suspend fun getEmotion() {
        useCase.getEmotion().collect {
            _emotion.emit(it)
        }
    }

    // EmotionText Result Flow
    private val _emotionText = MutableSharedFlow<String?>()
    val emotionText = _emotionText.asSharedFlow()

    suspend fun getEmotionText() {
        useCase.getEmotionText().collect {
            _emotionText.emit(it)
        }
    }

}