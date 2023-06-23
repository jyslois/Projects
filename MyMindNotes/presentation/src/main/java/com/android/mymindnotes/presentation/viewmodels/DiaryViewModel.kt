package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.GetDiaryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
  private val getDiaryListUseCase: GetDiaryListUseCase
): ViewModel() {

    sealed class DiaryUiState {
        data class Success(val getDiaryListResult: Map<String, Object>?): DiaryUiState()
        data class Error(val error: Boolean): DiaryUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<DiaryUiState>()
    val uiState: SharedFlow<DiaryUiState> = _uiState


    // getDiaryList() 함수 호출
    suspend fun getDiaryList() {
        getDiaryListUseCase().collect {
            _uiState.emit(DiaryUiState.Success(it))
        }
    }

    init {
        viewModelScope.launch {

            val getDiaryListFlow = getDiaryListUseCase().map { DiaryUiState.Success(it) }
            val getDiaryListErrorFlow = getDiaryListUseCase.error.map { DiaryUiState.Error(it) }

            merge(getDiaryListFlow, getDiaryListErrorFlow).collect {
                _uiState.emit(it)
            }
        }
    }

}