package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(

): ViewModel() {

    // 클릭 이벤트 플로우
    private val _recordDiaryButton = MutableSharedFlow<Boolean>()
    val recordDiaryButton = _recordDiaryButton.asSharedFlow()

    private val _diaryButton = MutableSharedFlow<Boolean>()
    val diaryButton = _diaryButton.asSharedFlow()

    private val _emotionInstructionButton = MutableSharedFlow<Boolean>()
    val emotionInstructionButton = _emotionInstructionButton.asSharedFlow()

    private val _accountSettingButton = MutableSharedFlow<Boolean>()
    val accountSettingButton = _accountSettingButton.asSharedFlow()

    private val _alarmSettingButton = MutableSharedFlow<Boolean>()
    val alarmSettingButton = _alarmSettingButton.asSharedFlow()

    // 클릭 이벤트 함수 콜
    suspend fun clickRecordDiaryButton() {
        _recordDiaryButton.emit(true)
    }

    suspend fun clickDiaryButton() {
        _diaryButton.emit(true)
    }

    suspend fun clickEmotionInstructionButton() {
        _emotionInstructionButton.emit(true)
    }

    suspend fun clickAccountSettingButton() {
        _accountSettingButton.emit(true)
    }

    suspend fun clickAlarmSettingButton() {
        _alarmSettingButton.emit(true)
    }

}