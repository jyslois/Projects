package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val savePasswordUseCase: SavePasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
): ViewModel() {

    sealed class ChangePasswordUiState {
        object Loading : ChangePasswordUiState()
        object Success: ChangePasswordUiState()
        data class Error(val error: String): ChangePasswordUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.Loading)
    val uiState: StateFlow<ChangePasswordUiState> = _uiState


    // (서버) 비밀번호 변경
    suspend fun changePasswordButtonClicked(password: String, originalPassword: String) {
        try {
            changePasswordUseCase(password, originalPassword).collect {
                if (it["code"].toString()
                        .toDouble() == 3005.0 || it["code"].toString()
                        .toDouble() == 3003.0
                ) {
                    _uiState.value = ChangePasswordUiState.Error(it["msg"] as String)
                    _uiState.value = ChangePasswordUiState.Loading
                } else if (it["code"].toString().toDouble() == 3002.0) {
                    savePasswordUseCase(password)
                    _uiState.value = ChangePasswordUiState.Success
                }
            }
        } catch (e: Exception) {
            _uiState.value = ChangePasswordUiState.Error("비밀번호 변경 실패. 인터넷 연결을 확인해 주세요.")
            _uiState.value = ChangePasswordUiState.Loading
        }
    }


}