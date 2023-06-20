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
import kotlinx.coroutines.flow.asSharedFlow
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

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 이메일
    // 이메일 중복 체크 버튼 클릭 감지 플로우
    private val _clickEmailCheck = MutableSharedFlow<Boolean>()
    val clickEmailCheck = _clickEmailCheck.asSharedFlow()

    // 이메일 중복 체크 버튼 클릭 감지
    suspend fun clickEmailCheckButton() {
        _clickEmailCheck.emit(true)
    }

    // SharedFlow
    // (서버) 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    val emailCheckResult = _emailCheckResult.asSharedFlow()


    // 이메일 중복 체크 함수 호출
    suspend fun checkEmail(emailInput: String) {
        checkEmailDuplicateUseCase(emailInput).collect {
          _emailCheckResult.emit(it)
      }
    }

    // 닉네임
    // 닉네임 중복 체크 버튼 클릭 감지 플로우
    private val _clickNickNameCheck = MutableSharedFlow<Boolean>()
    val clickNickNameCheck = _clickNickNameCheck.asSharedFlow()

    // 닉네임 중복 체크 버튼 클릭 감지
    suspend fun clickNickNameCheckButton() {
        _clickNickNameCheck.emit(true)
    }

    // 닉네임 중복 체크 결과 저장 플로우
    private val _nickNameCheckResult = MutableSharedFlow<Map<String, Object>>()
    val nickNameCheckResult = _nickNameCheckResult.asSharedFlow()


    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickName(nickNameInput: String) {
        checkNickNameDuplicateUseCase(nickNameInput).collect {
            _nickNameCheckResult.emit(it)
        }
    }


    // 회원가입
    // 회원가입 버튼 클릭 감지 플로우
    private val _clickJoinButton = MutableSharedFlow<Boolean>()
    val clickJoinButton = _clickJoinButton.asSharedFlow()

    // 회원가입 버튼 클릭 감지
    suspend fun clickJoinButton() {
        _clickJoinButton.emit(true)
    }

    // (서버) 회원가입 함수 호출
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int) {
        joinUseCase(email, nickname, password, birthyear).collect {
            _joinResult.emit(it)
        }
    }

    // 회원가입 결과 저장 플로우
    private val _joinResult = MutableSharedFlow<Map<String, Object>>()
    val joinResult = _joinResult.asSharedFlow()


    // collect & emit
    init {
        viewModelScope.launch {
            // 회원가입 에러 값 구독
            launch {
                joinUseCase.error.collect {
                    _error.emit(it)
                }
            }

            // duplicateCheck 에러 값 구독
            launch {
                checkEmailDuplicateUseCase.error.collect {
                    _error.emit(it)
                }
            }

            launch {
                checkNickNameDuplicateUseCase.error.collect {
                    _error.emit(it)
                }
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

    // textChange
    // textChange 감지를 위한 SharedFlow
    private val _emailInputTextChange = MutableSharedFlow<Boolean>()
    val emailInputTextChange = _emailInputTextChange.asSharedFlow()

    private val _nickNameInputTextChange = MutableSharedFlow<Boolean>()
    val nickNameInputTextChange = _nickNameInputTextChange.asSharedFlow()

    // textChange 감지 함수
    suspend fun emailInputTextChange() {
        _emailInputTextChange.emit(true)
    }

    suspend fun nickNameInputTextChange() {
        _nickNameInputTextChange.emit(true)
    }

}