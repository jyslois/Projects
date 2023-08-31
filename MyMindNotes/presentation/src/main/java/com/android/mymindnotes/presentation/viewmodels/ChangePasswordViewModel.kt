package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    sealed class ChangePasswordUiState {
        object Loading : ChangePasswordUiState()
        data class Success(val msg: String?) : ChangePasswordUiState()
        data class Error(val error: String) : ChangePasswordUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.Loading)
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    var errorStateTriggered = false // 테스트용

    // (서버) 비밀번호 변경
    suspend fun changePasswordButtonClicked(password: String, originalPassword: String) {

        changePasswordUseCase(password, originalPassword).collect {
            when {
                it.isSuccess -> _uiState.value = ChangePasswordUiState.Success(it.getOrNull())

                it.isFailure -> {
                    _uiState.value = ChangePasswordUiState.Error(it.exceptionOrNull()?.message ?: "비밀번호 변경 실패.")

                    // 테스트용
                    if (_uiState.value == ChangePasswordUiState.Error(it.exceptionOrNull()?.message ?: "비밀번호 변경 실패.")) {
                        errorStateTriggered = true
                    }

                    _uiState.value = ChangePasswordUiState.Loading
                }
            }
        }


    }
}