package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.diary.UpdateDiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryResultEditViewModel @Inject constructor(
    private val updateDiaryUseCase: UpdateDiaryUseCase
) : ViewModel() {

    sealed class DiaryResultEditUiState {
        object Loading : DiaryResultEditUiState()
        object Success : DiaryResultEditUiState()
        data class Error(val error: String) : DiaryResultEditUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryResultEditUiState>(DiaryResultEditUiState.Loading)
    val uiState: StateFlow<DiaryResultEditUiState> = _uiState


    // (네트워크) 일기 수정
    suspend fun updateDiaryButtonClicked(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ) {

        updateDiaryUseCase(
            diaryNumber,
            situation,
            thought,
            emotion,
            emotionDescription,
            reflection
        ).collect { result ->

            when (result) {
                is UpdateDiaryUseCase.UpdateDiaryResult.Success ->
                    _uiState.value = DiaryResultEditUiState.Success

                is UpdateDiaryUseCase.UpdateDiaryResult.Error -> {
                    _uiState.value = DiaryResultEditUiState.Error(result.message)
                    _uiState.value = DiaryResultEditUiState.Loading
                }
            }
        }
    }
}