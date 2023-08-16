package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.diary.UpdateDiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryResultEditViewModel @Inject constructor(
    private val updateDiaryUseCase: UpdateDiaryUseCase
) : ViewModel() {

    sealed class DiaryResultEditUiState {
        object Loading : DiaryResultEditUiState()
        data class Success(val msg: String?) : DiaryResultEditUiState()
        data class Error(val error: String) : DiaryResultEditUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryResultEditUiState>(DiaryResultEditUiState.Loading)
    val uiState: StateFlow<DiaryResultEditUiState> = _uiState.asStateFlow()


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
        ).collect {

            when {
                it.isSuccess -> _uiState.value = DiaryResultEditUiState.Success(it.getOrNull())

                it.isFailure -> {
                    _uiState.value = DiaryResultEditUiState.Error(it.exceptionOrNull()?.message ?: "일기 수정 실패. 인터넷 연결을 확인해 주세요.")

                    _uiState.value = DiaryResultEditUiState.Loading
                }
            }

        }
    }
}