package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.LoginUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ChangeAutoSaveBoxStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetIdUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getIdUseCase: GetIdUseCase,
    private val getPasswordUseCase: GetPasswordUseCase,
    private val getAutoLoginStateUseCase: GetAutoLoginStateUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val getAutoSaveStateUseCase: GetAutoSaveStateUseCase,
    private val changeAutoSaveBoxStateUseCase: ChangeAutoSaveBoxStateUseCase
) : ViewModel() {


    sealed class LoginUiState {
        object Loading: LoginUiState()
        data class Succeed(val autoSaveState: Boolean, val autoLoginState: Boolean, val id: String?, val password: String?): LoginUiState()

        object LoginSucceed: LoginUiState()
        data class Error(val error: String): LoginUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState


    // 로그인
    suspend fun loginButtonClicked(email: String, password: String, isAutoLoginChecked: Boolean, isAutoSaveChecked: Boolean) {

        loginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked).collect { result ->
            when (result) {
                is LoginUseCase.LoginResult.Success -> _uiState.value = LoginUiState.LoginSucceed
                is LoginUseCase.LoginResult.Error -> {
                    _uiState.value = LoginUiState.Error(result.message)
                    _uiState.value = LoginUiState.Loading
                }
            }
        }
    }

    fun autoSaveBoxClicked(isAutoSaveChecked: Boolean, isAutoLoginChecked: Boolean, id: String?, password: String?) {
        viewModelScope.launch {
            changeAutoSaveBoxStateUseCase(isAutoSaveChecked, isAutoLoginChecked, id, password)
        }
    }

    fun autoLoginBoxUnChecked() {
        viewModelScope.launch {
            saveAutoLoginStateUseCase(false)
        }
    }


    // ViewModel instance가 만들어질 때, autoSaveCheck/autoLoginCheck/id/password 값 불러오기
    init {
        viewModelScope.launch {

            val getAutoSaveStateFlow = getAutoSaveStateUseCase()
            val getAutoLoginStateFlow = getAutoLoginStateUseCase()
            val getIdFlow = getIdUseCase()
            val getPasswordFlow = getPasswordUseCase()

            combine(
                getAutoSaveStateFlow,
                getAutoLoginStateFlow,
                getIdFlow,
                getPasswordFlow
            ) { autoSaveState, autoLoginState, id, password ->
                LoginUiState.Succeed(autoSaveState, autoLoginState, id, password)
            }.collect { _uiState.emit(it) }

        }
    }
}