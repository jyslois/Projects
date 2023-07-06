package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.SaveTraumaDiaryUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryReflectionViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveTraumaDiaryUseCase,
    private val saveTraumaDiaryReflectionUseCase: SaveTraumaDiaryReflectionUseCase,
    private val getTraumaDiaryReflectionUseCase: GetTraumaDiaryReflectionUseCase
): ViewModel() {

    sealed class TraumaDiaryReflectionUiState {
        object Loading: TraumaDiaryReflectionUiState()
        data class Success(val reflection: String?): TraumaDiaryReflectionUiState()

        object DiarySaved: TraumaDiaryReflectionUiState()
        data class Error(val error: String): TraumaDiaryReflectionUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TraumaDiaryReflectionUiState>(TraumaDiaryReflectionUiState.Loading)
    val uiState: StateFlow<TraumaDiaryReflectionUiState> = _uiState

    suspend fun previousButtonClickedOrBackPressed(reflection: String) {
        saveTraumaDiaryReflectionUseCase(reflection)
        _uiState.value = TraumaDiaryReflectionUiState.Success(reflection)
    }

    // (서버) 일기 저장 함수 호출
    suspend fun saveDiaryButtonClicked(reflection: String?, type: String, date: String, day: String) {

        saveDiaryUseCase(reflection, type, date, day).collect {
            when {
                it.isSuccess -> _uiState.value = TraumaDiaryReflectionUiState.DiarySaved

                it.isFailure -> {
                    _uiState.value = TraumaDiaryReflectionUiState.Error(it.exceptionOrNull()?.message ?: "일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")
                    _uiState.value = TraumaDiaryReflectionUiState.Loading
                }
            }
        }
    }


    init {
        viewModelScope.launch {

            getTraumaDiaryReflectionUseCase().collect {
                _uiState.value = TraumaDiaryReflectionUiState.Success(it)
            }

        }
    }


}