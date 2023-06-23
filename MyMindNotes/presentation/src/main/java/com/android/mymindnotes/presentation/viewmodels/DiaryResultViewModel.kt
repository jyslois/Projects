package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.DeleteDiaryUseCase
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
class DiaryResultViewModel @Inject constructor(
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getDiaryListUseCase: GetDiaryListUseCase
) : ViewModel() {

    sealed class DiaryResultUiState {
        data class Success(val getDiaryListResult: Map<String, Object>?, val deleteDiaryListResult: Map<String, Object>?) : DiaryResultUiState()
        data class Error(val error: Boolean) : DiaryResultUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<DiaryResultUiState>()
    val uiState: SharedFlow<DiaryResultUiState> = _uiState


    // 다이어리 리스트 불러오기
    suspend fun getDiaryList() {
        getDiaryListUseCase().collect {
            _uiState.emit(DiaryResultUiState.Success(it, null))
        }
    }

    // 일기 삭제하기
    suspend fun deleteDiary(diaryNumber: Int) {
        deleteDiaryUseCase(diaryNumber).collect {
            _uiState.emit(DiaryResultUiState.Success(null, it))
        }
    }

    init {
        viewModelScope.launch {

            val getDiaryListErrorFlow = getDiaryListUseCase.error.map {
                DiaryResultUiState.Error(it)
            }
            val deleteDiaryListErrorFlow = deleteDiaryUseCase.error.map {
                DiaryResultUiState.Error(it)
            }

            merge(getDiaryListErrorFlow, deleteDiaryListErrorFlow).collect {
                _uiState.emit(it)
            }

        }

    }
}