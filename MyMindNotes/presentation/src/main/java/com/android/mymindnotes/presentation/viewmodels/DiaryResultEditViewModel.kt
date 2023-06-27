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
): ViewModel() {

    sealed class DiaryResultEditUiState {
        object Loading: DiaryResultEditUiState()
        object Success:  DiaryResultEditUiState()
        data class Error(val error: String):  DiaryResultEditUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryResultEditUiState>(DiaryResultEditUiState.Loading)
    val uiState: StateFlow<DiaryResultEditUiState> = _uiState


    // (네트워크) 일기 수정
    suspend fun updateDiary(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ) {
        try {
            updateDiaryUseCase(
                diaryNumber,
                situation,
                thought,
                emotion,
                emotionDescription,
                reflection
            ).collect {
                if (it["code"].toString().toDouble() == 8001.0) {

                    _uiState.value = DiaryResultEditUiState.Error(it["msg"] as String)

                } else if (it["code"].toString().toDouble() == 8000.0) {

                    _uiState.emit(DiaryResultEditUiState.Success)
                }
            }
        } catch(e: Exception) {
            _uiState.value = DiaryResultEditUiState.Error("일기 수정 실패. 인터넷 연결을 확인해 주세요.")
            _uiState.value = DiaryResultEditUiState.Loading
        }
    }


}