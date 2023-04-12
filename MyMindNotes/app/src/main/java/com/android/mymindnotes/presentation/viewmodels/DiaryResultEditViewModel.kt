package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.UpdateDiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryResultEditViewModel @Inject constructor(
    private val updateDiaryUseCase: UpdateDiaryUseCase
): ViewModel() {

    // 에러 감지 플로우
    private val _updateDiaryError = MutableSharedFlow<Boolean>()
    val updateDiaryError = _updateDiaryError.asSharedFlow()

    // 버튼 클릭 감지
    // 감정 설명 버튼 클릭 감지 플로우
    private val _emotionHelp = MutableSharedFlow<Boolean>()
    val emotionHelp = _emotionHelp.asSharedFlow()

    suspend fun clickEmotionHelp() {
        _emotionHelp.emit(true)
    }

    // 일기 수정 버튼 클릭 감지 플로우
    private val _editButton = MutableSharedFlow<Boolean>()
    val editButton = _editButton.asSharedFlow()

    suspend fun clickEditButton() {
        _editButton.emit(true)
    }

    // 일기 수정 결과 저장 플로우
    private val _updateDiaryResult = MutableSharedFlow<Map<String, Object>>()
    val updateDiaryResult = _updateDiaryResult.asSharedFlow()

    // (네트워크) 일기 수정
    suspend fun updateDiary(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ) {
        updateDiaryUseCase.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection).collect {
            _updateDiaryResult.emit(it)
        }
    }

    init {
        viewModelScope.launch {
            // 에러 구독 & emit
            updateDiaryUseCase.updateDiaryError.collect {
                _updateDiaryError.emit(it)
            }
        }
    }


}