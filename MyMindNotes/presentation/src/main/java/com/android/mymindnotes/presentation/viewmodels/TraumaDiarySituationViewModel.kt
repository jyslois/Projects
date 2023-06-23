package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiarySituationUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiarySituationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiarySituationViewModel @Inject constructor(
    private val saveTraumaDiarySituationUseCase: SaveTraumaDiarySituationUseCase,
    private val getTraumaDiarySituationUseCase: GetTraumaDiarySituationUseCase,
    private val clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
): ViewModel() {


    sealed class TraumaDiarySituationUiState {
        data class Success(val situationResult: String?): TraumaDiarySituationUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TraumaDiarySituationUiState>()
    val uiState: SharedFlow<TraumaDiarySituationUiState> = _uiState


    // Save Method
    suspend fun saveSituation(situation: String) {
        saveTraumaDiarySituationUseCase(situation)
    }


    // Clear Methods
    suspend fun clearTraumaDiaryTempRecords() {
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