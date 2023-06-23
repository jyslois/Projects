package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val savePasswordUseCase: SavePasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
): ViewModel() {

    sealed class ChangePasswordUiState {
        data class Success(val changePasswordResult: Map<String, Object>?): ChangePasswordUiState()
        data class Error(val error: Boolean): ChangePasswordUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<ChangePasswordUiState>()
    val uiState: SharedFlow<ChangePasswordUiState> = _uiState


    // (서버) 비밀번호 변경
    suspend fun changePassword(password: String, originalPassword: String) {
        changePasswordUseCase(password, originalPassword).collect {
            _uiState.emit(ChangePasswordUiState.Success(it))
        }
    }

    // 비밀번호 재저장
    suspend fun savePassword(password: String?) {
        savePasswordUseCase(password)
    }

    init {
        viewModelScope.launch {
            // 비밀번호 변경 에러 값 구독
            launch {
                changePasswordUseCase.error.collect {
                    _uiState.emit(ChangePasswordUiState.Error(it))
                }
            }
        }
    }

}