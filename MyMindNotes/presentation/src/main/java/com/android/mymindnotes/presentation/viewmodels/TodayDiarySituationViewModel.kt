package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiarySituationUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiarySituationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiarySituationViewModel @Inject constructor(
    private val saveTodayDiarySituationUseCase: SaveTodayDiarySituationUseCase,
    private val getTodayDiarySituationUseCase: GetTodayDiarySituationUseCase
): ViewModel() {

    sealed class TodayDiarySituationUiState {
        object Loading: TodayDiarySituationUiState()
        data class Success(val situation: String?): TodayDiarySituationUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TodayDiarySituationUiState>(TodayDiarySituationUiState.Loading)
    val uiState: StateFlow<TodayDiarySituationUiState> = _uiState

    // Save Method
    suspend fun nextOrPreviousButtonClickedOrBackPressedOrPaused(situation: String) {
        saveTodayDiarySituationUseCase(situation)
    }

    init {
        viewModelScope.launch {
            getTodayDiarySituationUseCase().collect {
                _uiState.emit(TodayDiarySituationUiState.Success(it))
            }
        }
    }

}