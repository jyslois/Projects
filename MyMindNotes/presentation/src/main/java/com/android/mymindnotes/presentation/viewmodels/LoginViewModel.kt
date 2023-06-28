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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.lang.Exception
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
        object Loading: LoginUiState()
        data class Succeed(val autoSaveState: Boolean, val autoLoginState: Boolean, val id: String?, val password: String?): LoginUiState()

        object LoginSucceed: LoginUiState()
        data class Error(val error: String): LoginUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState




    // 로그인
    suspend fun login(email: String, password: String, isAutoLoginChecked: Boolean, isAutoSaveChecked: Boolean) {
        try {
            login_useCase(email, password).collect {
                if (it["code"].toString()
                        .toDouble() == 5001.0 || it["code"].toString()
                        .toDouble() == 5003.0
                    || it["code"].toString().toDouble() == 5005.0
                ) {
                    // 형식 오류/아이디와 비번 불일치로 인한 메시지
                    _uiState.value = LoginUiState.Error(it["msg"] as String)
                    _uiState.value = LoginUiState.Loading
                } else if (it["code"].toString().toDouble() == 5000.0) {
                    // 로그인 성공
                    if (isAutoLoginChecked) {
                        // MainActivity에서의 자동 로그인을 위한 상태 저장
                        saveAutoLoginStateUseCase(true)
                    }

                    if (isAutoSaveChecked) {
                        saveAutoSaveStateUseCase(true)
                        saveIdAndPasswordUseCase(email, password)
                    } else {
                        saveAutoSaveStateUseCase(false)
                        saveIdAndPasswordUseCase(null, null)
                    }

                    // 회원 번호 저장
                    saveUserIndexUseCase(it["user_index"].toString().toDouble().toInt())

                    _uiState.value = LoginUiState.LoginSucceed
                }
            }
        } catch (e: Exception) {
            _uiState.value = LoginUiState.Error("로그인에 실패했습니다. 인터넷 연결을 확인해 주세요.")
            _uiState.value = LoginUiState.Loading
        }

    }


    // save methods - SharedPreferneces
    suspend fun saveAutoLoginCheck(state: Boolean) {
        saveAutoLoginStateUseCase(state)
    }

    suspend fun saveAutoSaveCheck(isAutoSaveChecked: Boolean, isAutoLoginChecked: Boolean, id: String?, password: String?) {

        if (isAutoSaveChecked || isAutoLoginChecked) {
            saveAutoSaveStateUseCase(true)
            saveIdAndPasswordUseCase(id, password)
        } else {
            saveAutoSaveStateUseCase(false)
            saveIdAndPasswordUseCase(null, null)
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