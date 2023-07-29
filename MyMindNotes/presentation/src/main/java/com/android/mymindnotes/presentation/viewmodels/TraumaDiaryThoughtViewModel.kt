package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryThoughtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryThoughtViewModel @Inject constructor(
    private val traumaDiaryThoughtUseCase: SaveTraumaDiaryThoughtUseCase,
    private val getTraumaDiaryThoughtUseCase: GetTraumaDiaryThoughtUseCase
): ViewModel() {

    sealed class TraumaDiaryThoughtUiState {
        object Loading: TraumaDiaryThoughtUiState()
        data class Success(val thought: String?): TraumaDiaryThoughtUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TraumaDiaryThoughtUiState>(TraumaDiaryThoughtUiState.Loading)
    val uiState: StateFlow<TraumaDiaryThoughtUiState> = _uiState.asStateFlow()

    // Save Method
    suspend fun previousOrNextButtonClickedOrBackPressed(thought: String) {
        traumaDiaryThoughtUseCase(thought)
        _uiState.emit(TraumaDiaryThoughtUiState.Success(thought))
    }

    init {
        viewModelScope.launch {
            getTraumaDiaryThoughtUseCase().collect {
                _uiState.emit(TraumaDiaryThoughtUiState.Success(it))
            }
        }
    }

}