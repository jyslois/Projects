package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeToTemporaryPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val changeToTemporaryPasswordUseCase: ChangeToTemporaryPasswordUseCase
): ViewModel() {


    sealed class FindPasswordUiState {
        data class Success(val changeToTemporaryPasswordResult: Map<String, Object>?): FindPasswordUiState()
        data class Error(val error: Boolean): FindPasswordUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<FindPasswordUiState>()
    val uiState: SharedFlow<FindPasswordUiState> = _uiState


    // (서버) 임시 비밀번호로 비밀번호 변경하기
    suspend fun changeToTemporaryPassword(email: String, randomPassword: String) {
        changeToTemporaryPasswordUseCase(email, randomPassword).collect {
            _uiState.emit(FindPasswordUiState.Success(it))
        }
    }

    init {
        viewModelScope.launch {
            // 비밀번호 변경 에러 값 구독
            launch {
                changeToTemporaryPasswordUseCase.error.collect {
                    _uiState.emit(FindPasswordUiState.Error(it))
                }
            }
        }
    }

}