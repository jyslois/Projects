package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryThoughtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryThoughtViewModel @Inject constructor(
    private val saveTodayDiaryThoughtUseCase: SaveTodayDiaryThoughtUseCase,
    private val getTodayDiaryThoughtUseCase: GetTodayDiaryThoughtUseCase
): ViewModel() {

    sealed class TodayDiaryThoughtUiState {
        data class Success(val thoughtResult: String?): TodayDiaryThoughtUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TodayDiaryThoughtUiState>()
    val uiState: SharedFlow<TodayDiaryThoughtUiState> = _uiState

    // Save Method
    suspend fun saveThought(thought: String) {
        saveTodayDiaryThoughtUseCase(thought)
    }

    init {
        viewModelScope.launch {
            getTodayDiaryThoughtUseCase().collect {
                _uiState.emit(TodayDiaryThoughtUiState.Success(it))
            }
        }
    }

}