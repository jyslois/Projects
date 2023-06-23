package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.UpdateDiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryResultEditViewModel @Inject constructor(
    private val updateDiaryUseCase: UpdateDiaryUseCase
): ViewModel() {

    sealed class DiaryResultEditUiState {
        data class Success(val updateDiaryResult: Map<String, Object>?):  DiaryResultEditUiState()
        data class Error(val error: Boolean):  DiaryResultEditUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<DiaryResultEditUiState>()
    val uiState: SharedFlow<DiaryResultEditUiState> = _uiState


    // (네트워크) 일기 수정
    suspend fun updateDiary(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ) {
        updateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection).collect {
            _uiState.emit(DiaryResultEditUiState.Success(it))
        }
    }

    init {
        viewModelScope.launch {
            // 에러 구독 & emit
            updateDiaryUseCase.updateDiaryError.collect {
                _uiState.emit(DiaryResultEditUiState.Error(it))
            }
        }
    }


}