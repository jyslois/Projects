package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.domain.usecases.diaryRemote.DeleteDiaryUseCase
import com.android.mymindnotes.domain.usecases.diaryRemote.GetDiaryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryResultViewModel @Inject constructor(
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getDiaryListUseCase: GetDiaryListUseCase
) : ViewModel() {

    sealed class DiaryResultUiState {
        object Loading : DiaryResultUiState()
        object Finish : DiaryResultUiState()
        data class Success(val diaryList: ArrayList<Diary>?) : DiaryResultUiState()
        data class Error(val error: String) : DiaryResultUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryResultUiState>(DiaryResultUiState.Loading)
    val uiState: StateFlow<DiaryResultUiState> = _uiState.asStateFlow()

    var errorStateTriggered = false // 테스트용

    // 다이어리 리스트 불러오기
    suspend fun getDiaryList() {

        getDiaryListUseCase().collect {
            when {
                it.isSuccess -> {
                    _uiState.value = DiaryResultUiState.Success(it.getOrNull()?.diaryList)
                }

                it.isFailure -> {
                    _uiState.value = DiaryResultUiState.Error("일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")

                    // 테스트
                    if (_uiState.value == DiaryResultUiState.Error("일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")) {
                        errorStateTriggered = true
                    }

                    _uiState.value = DiaryResultUiState.Loading
                }
            }
        }

    }

    // 일기 삭제하기
    suspend fun deleteDiaryButtonClicked(diaryNumber: Int) {

        deleteDiaryUseCase(diaryNumber).collect {
            when {
                it.isSuccess -> _uiState.value = DiaryResultUiState.Finish

                it.isFailure -> {
                    _uiState.value = DiaryResultUiState.Error("일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")

                    // 테스트
                    if (_uiState.value == DiaryResultUiState.Error("일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")) {
                        errorStateTriggered = true
                    }

                    _uiState.value = DiaryResultUiState.Loading
                }
            }
        }

    }

}