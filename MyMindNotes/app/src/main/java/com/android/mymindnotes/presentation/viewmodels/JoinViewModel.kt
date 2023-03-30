package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.DuplicateCheckUseCase
import com.android.mymindnotes.domain.usecase.JoinUseCase
import com.android.mymindnotes.domain.usecase.UseSharedPreferencesUseCase
import com.android.mymindnotes.hilt.module.IoDispatcher
import com.bumptech.glide.Glide.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val duplicateCheckUseCase: DuplicateCheckUseCase,
    private val useSharedPreferencesUseCase: UseSharedPreferencesUseCase,
    private val joinUseCase: JoinUseCase
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
      duplicateCheckUseCase.checkEmail(emailInput).collect {
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
        duplicateCheckUseCase.checkNickName(nickNameInput).collect {
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
        joinUseCase.join(email, nickname, password, birthyear)
    }

    // 회원가입 결과 저장 플로우
    private val _joinResult = MutableSharedFlow<Map<String, Object>>()
    val joinResult = _joinResult.asSharedFlow()


    // collect & emit
    init {
        viewModelScope.launch {
            // 회원가입 결과 플로우 구독
            launch {
                joinUseCase.joinResult.collect {
                    _joinResult.emit(it)
                }
            }

            // duplicateCheck 에러 값 구독
            launch {
                duplicateCheckUseCase.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

    // 처음으로 로그인한 유저인지 판단을 위한 값을 SharedPreferences에 저장하기 위해 함수 호출
    suspend fun saveFirstTime(boolean: Boolean) {
        useSharedPreferencesUseCase.saveFirstTime(boolean)
    }

    // 회원 번호를 SharedPreference에 저장하기 위한 함수 호출
    suspend fun saveUserindex(userIndex: Int) {
        useSharedPreferencesUseCase.saveUserIndex(userIndex)
    }

    // 아이디와 비밀번호를 SharedPreference에 저장하기 위한 함수 호출
    suspend fun saveIdAndPassword(id: String, password: String) {
        useSharedPreferencesUseCase.saveIdAndPassword(id, password)
    }

    // AutoSave & AutoLogin Check 상태를 저장하기 위한 함수 호출
    suspend fun saveAutoLoginCheck(state: Boolean) {
        useSharedPreferencesUseCase.saveAutoLoginCheck(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        useSharedPreferencesUseCase.saveAutoSaveCheck(state)
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