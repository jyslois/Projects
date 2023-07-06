package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.SaveTodayDiaryUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryReflectionViewModel @Inject constructor(
    private val saveTodayDiaryUseCase: SaveTodayDiaryUseCase,
    private val saveTodayDiaryReflectionUseCase: SaveTodayDiaryReflectionUseCase,
    private val getTodayDiaryReflectionUseCase: GetTodayDiaryReflectionUseCase
): ViewModel() {

    sealed class TodayDiaryReflectionUiState {
        object Loading: TodayDiaryReflectionUiState()
        data class Success(val reflection: String?): TodayDiaryReflectionUiState()

        object DiarySaved: TodayDiaryReflectionUiState()
        data class Error(val error: String): TodayDiaryReflectionUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TodayDiaryReflectionUiState>(TodayDiaryReflectionUiState.Loading)
    val uiState: StateFlow<TodayDiaryReflectionUiState> = _uiState


    suspend fun previousButtonClickedOrBackPressed(reflection: String) {
        saveTodayDiaryReflectionUseCase(reflection)
        _uiState.value = TodayDiaryReflectionUiState.Success(reflection)
    }


    // (서버) 일기 저장 함수 호출
    suspend fun saveDiaryButtonClicked(reflection: String?, type: String, date: String, day: String) {

        saveTodayDiaryUseCase(reflection, type, date, day).collect {
            when {
                it.isSuccess ->  _uiState.value = TodayDiaryReflectionUiState.DiarySaved

                it.isFailure -> {
                    _uiState.value = TodayDiaryReflectionUiState.Error(it.exceptionOrNull()?.message ?: "일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")
                    _uiState.value = TodayDiaryReflectionUiState.Loading
                }
            }
        }
    }

    init {
        viewModelScope.launch {

            getTodayDiaryReflectionUseCase().collect {
                _uiState.value = TodayDiaryReflectionUiState.Success(it)
            }

        }
    }

}