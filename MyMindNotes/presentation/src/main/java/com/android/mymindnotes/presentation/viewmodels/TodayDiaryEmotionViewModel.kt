package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionPartsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionPartsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TodayDiaryEmotionViewModel @Inject constructor(
    private val saveTodayDiaryEmotionPartsUseCase: SaveTodayDiaryEmotionPartsUseCase,
    private val getTodayDiaryEmotionPartsUseCase: GetTodayDiaryEmotionPartsUseCase,
    private val clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
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

    // Emotion 클릭 감지
    private val _emotionGroup1Click = MutableStateFlow<Int>(-1)
    val emotionGroup1Click = _emotionGroup1Click.asStateFlow()

    private val _emotionGroup2Click = MutableStateFlow<Int>(-1)
    val emotionGroup2Click = _emotionGroup2Click.asStateFlow()

    // Emotion 버튼 클릭
    suspend fun clickEmotionGroup1(checkedId: Int) {
        _emotionGroup1Click.emit(checkedId)
    }

    suspend fun clickEmotionGroup2(checkedId: Int) {
        _emotionGroup2Click.emit(checkedId)
    }

    // Save Methods
    suspend fun saveEmotionColor(color: Int) {
        saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(color)
    }

    suspend fun saveEmotion(emotion: String) {
        saveTodayDiaryEmotionPartsUseCase.saveEmotion(emotion)
    }

    suspend fun saveEmotionText(emotionText: String?) {
        saveTodayDiaryEmotionPartsUseCase.saveEmotionText(emotionText)
    }

    // Get Methods

    // Emotion Result Flow
    private val _emotion = MutableSharedFlow<String?>()
    val emotion = _emotion.asSharedFlow()

    suspend fun getEmotion() {
        getTodayDiaryEmotionPartsUseCase.getEmotion().collect {
            _emotion.emit(it)
        }
    }

    // EmotionText Result Flow
    private val _emotionText = MutableSharedFlow<String?>()
    val emotionText = _emotionText.asSharedFlow()

    suspend fun getEmotionText() {
        getTodayDiaryEmotionPartsUseCase.getEmotionText().collect {
            _emotionText.emit(it)
        }
    }

    // Clear Methods
    suspend fun clearTodayDiaryTempRecords() {
        clearTodayDiaryTempRecordsUseCase()
    }


}