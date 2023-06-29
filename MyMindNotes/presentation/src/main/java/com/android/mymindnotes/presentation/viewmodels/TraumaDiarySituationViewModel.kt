package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiarySituationUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiarySituationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiarySituationViewModel @Inject constructor(
    private val saveTraumaDiarySituationUseCase: SaveTraumaDiarySituationUseCase,
    private val getTraumaDiarySituationUseCase: GetTraumaDiarySituationUseCase,
    private val clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
): ViewModel() {


    sealed class TraumaDiarySituationUiState {
        object Loading: TraumaDiarySituationUiState()
        data class Success(val situation: String?): TraumaDiarySituationUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TraumaDiarySituationUiState>(TraumaDiarySituationUiState.Loading)
    val uiState: StateFlow<TraumaDiarySituationUiState> = _uiState


    // Save Method
    suspend fun nextButtonClicked(situation: String) {
        saveTraumaDiarySituationUseCase(situation)
        _uiState.emit(TraumaDiarySituationUiState.Success(situation))
    }


    // Clear Methods
    suspend fun onBackPressed() {
        clearTraumaDiaryTempRecordsUseCase()
    }

    init {
        viewModelScope.launch {
            getTraumaDiarySituationUseCase().collect {
                _uiState.emit(TraumaDiarySituationUiState.Success(it))
            }
        }
    }


}