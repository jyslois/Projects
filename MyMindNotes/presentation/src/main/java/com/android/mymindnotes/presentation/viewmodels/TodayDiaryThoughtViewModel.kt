package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryThoughtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryThoughtViewModel @Inject constructor(
    private val saveTodayDiaryThoughtUseCase: SaveTodayDiaryThoughtUseCase,
    private val getTodayDiaryThoughtUseCase: GetTodayDiaryThoughtUseCase
): ViewModel() {

    sealed class TodayDiaryThoughtUiState {
        object Loading: TodayDiaryThoughtUiState()
        data class Success(val thought: String?): TodayDiaryThoughtUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TodayDiaryThoughtUiState>(TodayDiaryThoughtUiState.Loading)
    val uiState: StateFlow<TodayDiaryThoughtUiState> = _uiState.asStateFlow()

    // Save Method
    suspend fun nextOrPreviousButtonClickedOrBackPressedOrOnPause(thought: String) {
        saveTodayDiaryThoughtUseCase(thought)
        _uiState.emit(TodayDiaryThoughtUiState.Success(thought))
    }

    init {
        viewModelScope.launch {
            getTodayDiaryThoughtUseCase().collect {
                _uiState.emit(TodayDiaryThoughtUiState.Success(it))
            }
        }
    }

}