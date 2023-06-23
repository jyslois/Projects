package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.JoinUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckEmailDuplicateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val joinUseCase: JoinUseCase,

    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val saveFirstTimeStateUseCase: SaveFirstTimeStateUseCase,
    private val saveUserIndexUseCase: SaveUserIndexUseCase,

    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase,

    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase
) : ViewModel() {

    sealed class JoinUiState {
        data class Success(val emailDuplicateCheckResult: Map<String, Object>?, val nickNameDuplicateCheckResult: Map<String, Object>?, val joinResult: Map<String, Object>?): JoinUiState()
        data class Error(val error: Boolean): JoinUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<JoinUiState>()
    val uiState: SharedFlow<JoinUiState> = _uiState


    // 이메일 중복 체크 함수 호출
    suspend fun checkEmail(emailInput: String) {
        checkEmailDuplicateUseCase(emailInput).collect {
          _uiState.emit(JoinUiState.Success(it, null, null))
      }
    }


    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickName(nickNameInput: String) {
        checkNickNameDuplicateUseCase(nickNameInput).collect {
            _uiState.emit(JoinUiState.Success(null, it, null))
        }
    }


    // (서버) 회원가입 함수 호출
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int) {
        joinUseCase(email, nickname, password, birthyear).collect {
            _uiState.emit(JoinUiState.Success(null, null, it))
        }
    }


    // collect & emit
    init {
        viewModelScope.launch {

            val joinErrorFlow =  joinUseCase.error.map { JoinUiState.Error(it) }
            val emailDuplicateCheckErrorFlow = checkEmailDuplicateUseCase.error.map { JoinUiState.Error(it) }
            val nickNameDuplicateCheckErrorFlow = checkNickNameDuplicateUseCase.error.map { JoinUiState.Error(it) }

            merge(joinErrorFlow, emailDuplicateCheckErrorFlow, nickNameDuplicateCheckErrorFlow).collect {
                _uiState.emit(it)

            }
        }
    }

    // 처음으로 로그인한 유저인지 판단을 위한 값을 SharedPreferences에 저장하기 위해 함수 호출
    suspend fun saveFirstTime(boolean: Boolean) {
        saveFirstTimeStateUseCase(boolean)
    }

    // 회원 번호를 SharedPreference에 저장하기 위한 함수 호출
    suspend fun saveUserindex(userIndex: Int) {
        saveUserIndexUseCase(userIndex)
    }

    // 아이디와 비밀번호를 SharedPreference에 저장하기 위한 함수 호출
    suspend fun saveIdAndPassword(id: String, password: String) {
        saveIdAndPasswordUseCase(id, password)
    }

    // AutoSave & AutoLogin Check 상태를 저장하기 위한 함수 호출
    suspend fun saveAutoLoginCheck(state: Boolean) {
        saveAutoLoginStateUseCase(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        saveAutoSaveStateUseCase(state)
    }


}