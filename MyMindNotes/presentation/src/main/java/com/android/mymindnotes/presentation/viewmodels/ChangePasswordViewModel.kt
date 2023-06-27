package com.android.mymindnotes.presentation.viewmodels

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangePasswordUseCase
import com.android.mymindnotes.presentation.ui.MainPageActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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
    suspend fun changePassword(password: String, originalPassword: String) {
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