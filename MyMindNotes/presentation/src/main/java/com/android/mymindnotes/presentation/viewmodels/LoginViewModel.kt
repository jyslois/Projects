package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.LogInUseCase
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetIdUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login_useCase: LogInUseCase,

    private val getIdUseCase: GetIdUseCase,
    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val getPasswordUseCase: GetPasswordUseCase,
    private val saveUserIndexUseCase: SaveUserIndexUseCase,

    private val getAutoLoginStateUseCase: GetAutoLoginStateUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val getAutoSaveStateUseCase: GetAutoSaveStateUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
) : ViewModel() {


    sealed class LoginUiState {
        data class Success(val loginResult: Map<String, Object>?): LoginUiState()
        data class AutoSaveState(val isChecked: Boolean): LoginUiState()
        data class AutoLoginState(val isChecked: Boolean): LoginUiState()

        data class Id(val id: String?): LoginUiState()

        data class Password(val password: String?): LoginUiState()
        data class Error(val error: Boolean): LoginUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<LoginUiState>()
    val uiState: SharedFlow<LoginUiState> = _uiState




    // 로그인
    suspend fun login(email: String, password: String) {
        login_useCase(email, password).collect {
            _uiState.emit(LoginUiState.Success(it))
        }
    }


    // save methods - SharedPreferneces
    suspend fun saveAutoLoginCheck(state: Boolean) {
        saveAutoLoginStateUseCase(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        saveAutoSaveStateUseCase(state)
    }

    suspend fun saveIdAndPassword(id: String?, password: String?) {
        saveIdAndPasswordUseCase(id, password)
    }

    suspend fun saveUserIndex(index: Int) {
        saveUserIndexUseCase(index)
    }

    // ViewModel instance가 만들어질 때, autoSaveCheck/autoLoginCheck/id/password 값 불러오기
    init {
        viewModelScope.launch {

            val getAutoSaveStateFlow = getAutoSaveStateUseCase().map { LoginUiState.AutoSaveState(it) }
            val getAutoLoginStateFlow = getAutoLoginStateUseCase().map { LoginUiState.AutoLoginState(it) }
            val getIdFlow = getIdUseCase().map { LoginUiState.Id(it) }
            val getPasswordFlow = getPasswordUseCase().map { LoginUiState.Password(it) }
            val getLoginErrorFlow = login_useCase.error.map { LoginUiState.Error(it) }

            merge(getAutoSaveStateFlow, getAutoLoginStateFlow, getIdFlow, getPasswordFlow, getLoginErrorFlow).collect {
                _uiState.emit(it)
            }
        }
    }
}