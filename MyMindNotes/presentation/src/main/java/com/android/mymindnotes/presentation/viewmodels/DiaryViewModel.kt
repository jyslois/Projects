package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.domain.usecases.diaryRemote.GetDiaryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
  private val getDiaryListUseCase: GetDiaryListUseCase
): ViewModel() {

    sealed class DiaryUiState {
        object Loading: DiaryUiState()
        data class Success(val diaryList: ArrayList<Diary>?): DiaryUiState()
        data class Error(val error: String): DiaryUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryUiState>(DiaryUiState.Loading)
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()


    // getDiaryList() 함수 호출
    suspend fun getDiaryList() {

        getDiaryListUseCase().collect {
            when {
                it.isSuccess -> {
                    _uiState.value = DiaryUiState.Success(it.getOrNull()?.diaryList)
                }

                it.isFailure -> {
                    _uiState.value = DiaryUiState.Error(it.exceptionOrNull()?.message ?: "일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")
                    _uiState.value = DiaryUiState.Loading
                }
            }
        }

    }

    init {
        viewModelScope.launch {
            getDiaryList()
        }
    }

}