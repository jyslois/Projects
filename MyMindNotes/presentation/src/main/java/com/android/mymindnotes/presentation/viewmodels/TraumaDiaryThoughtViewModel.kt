package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryThoughtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryThoughtViewModel @Inject constructor(
    private val traumaDiaryThoughtUseCase: SaveTraumaDiaryThoughtUseCase,
    private val getTraumaDiaryThoughtUseCase: GetTraumaDiaryThoughtUseCase
): ViewModel() {

    sealed class TraumaDiaryThoughtUiState {
        data class Success(val thoughtResult: String?): TraumaDiaryThoughtUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TraumaDiaryThoughtUiState>()
    val uiState: SharedFlow<TraumaDiaryThoughtUiState> = _uiState

    // Save Method
    suspend fun saveThought(thought: String) {
        traumaDiaryThoughtUseCase(thought)
    }

    init {
        viewModelScope.launch {
            getTraumaDiaryThoughtUseCase().collect {
                _uiState.emit(TraumaDiaryThoughtUiState.Success(it))
            }
        }
    }

}