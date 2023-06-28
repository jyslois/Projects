package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.JoinUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckEmailDuplicateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        object Loading: JoinUiState()
        object EmailDuplicateCheckSucceed: JoinUiState()
        object NicknameDuplicateCheckSucceed: JoinUiState()
        object JoinSucceed: JoinUiState()
        data class Error(val error: String): JoinUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<JoinUiState>(JoinUiState.Loading)
    val uiState: StateFlow<JoinUiState> = _uiState


    // 이메일 중복 체크 함수 호출
    suspend fun checkEmailButtonClicked(emailInput: String) {
        try {
            checkEmailDuplicateUseCase(emailInput).collect {
                if (it["code"].toString().toDouble() == 1001.0) {
                    _uiState.value = JoinUiState.Error(it["msg"] as String)
                } else if (it["code"].toString().toDouble() == 1000.0) {
                    _uiState.value = JoinUiState.EmailDuplicateCheckSucceed
                }
            }
        } catch (e: Exception) {
            _uiState.value = JoinUiState.Error("이메일 중복 체크에 실패했습니다. 인터넷 연결을 확인해 주세요.")
        }

        _uiState.value = JoinUiState.Loading
    }


    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickNameButtonClicked(nickNameInput: String) {
        try {
            checkNickNameDuplicateUseCase(nickNameInput).collect {
                if (it["code"].toString().toDouble() == 1003.0) {
                    _uiState.value = JoinUiState.Error(it["msg"] as String)
                } else if (it["code"].toString().toDouble() == 1002.0) {
                    _uiState.value = JoinUiState.NicknameDuplicateCheckSucceed
                }
            }
        } catch (e: Exception) {
            _uiState.value = JoinUiState.Error("닉네임 중복 체크에 실패했습니다. 인터넷 연결을 확인해 주세요.")
        }

        _uiState.value = JoinUiState.Loading
    }


    // (서버) 회원가입 함수 호출
    suspend fun joinButtonClicked(email: String, nickname: String, password: String, birthyear: Int) {
        try {
            joinUseCase(email, nickname, password, birthyear).collect {
                if (it["code"].toString().toDouble() == 2001.0) {
                    _uiState.value = JoinUiState.Error(it["msg"] as String)
                } else if (it["code"].toString().toDouble() == 2000.0) {
                    // 회원 번호 저장
                    saveUserIndexUseCase(it["user_index"].toString().toDouble().toInt())

                    // 아이디와 비밀번호 저장
                    saveIdAndPasswordUseCase(email, password)

                    // 아이디/비밀번호 저장 체크 박스 상태를 true로 저장, 자동 로그인 설정
                    saveAutoSaveStateUseCase(true)
                    saveAutoLoginStateUseCase(true)

                    // 회원가입 후 최초 로그인시 알람 설정 다이얼로그를 띄우기 위한 sharedPreferences
                    saveFirstTimeStateUseCase(true)

                    _uiState.value = JoinUiState.JoinSucceed
                }
            }
        } catch (e: Exception) {
            _uiState.value = JoinUiState.Error("회원가입에 실패했습니다. 인터넷 연결을 확인해 주세요.")
        }

        _uiState.value = JoinUiState.Loading
    }


}