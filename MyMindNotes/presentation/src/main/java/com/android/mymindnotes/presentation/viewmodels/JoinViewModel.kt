package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.JoinUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckEmailDuplicateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val joinUseCase: JoinUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase
) : ViewModel() {

    sealed class JoinUiState {
        object Loading : JoinUiState()
        object EmailDuplicateCheckSucceed : JoinUiState()
        object NicknameDuplicateCheckSucceed : JoinUiState()
        object JoinSucceed : JoinUiState()
        data class Error(val error: String) : JoinUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<JoinUiState>(JoinUiState.Loading)
    val uiState: StateFlow<JoinUiState> = _uiState.asStateFlow()


    // 이메일 중복 체크 함수 호출
    suspend fun checkEmailButtonClicked(emailInput: String) {

        checkEmailDuplicateUseCase(emailInput).collect {
            when {
                it.isSuccess -> _uiState.value = JoinUiState.EmailDuplicateCheckSucceed

                it.isFailure -> _uiState.value = JoinUiState.Error(it.exceptionOrNull()?.message ?: "이메일 중복 체크에 실패했습니다. 인터넷 연결을 확인해 주세요.")
            }
        }
        _uiState.value = JoinUiState.Loading
    }


    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickNameButtonClicked(nickNameInput: String) {
        // (서버) 닉네임 중복 체크
        checkNickNameDuplicateUseCase(nickNameInput).collect {
            when {
                it.isSuccess -> _uiState.value = JoinUiState.NicknameDuplicateCheckSucceed

                it.isFailure -> _uiState.value = JoinUiState.Error(it.exceptionOrNull()?.message ?: "닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요.")
            }
        }
        _uiState.value = JoinUiState.Loading
    }


    // (서버) 회원가입 함수 호출
    suspend fun joinButtonClicked(
        email: String,
        nickname: String,
        password: String,
        birthyear: Int
    ) {

        joinUseCase(email, nickname, password, birthyear).collect {
            when {
                it.isSuccess -> _uiState.value = JoinUiState.JoinSucceed

                it.isFailure ->  _uiState.value = JoinUiState.Error(it.exceptionOrNull()?.message ?: "회원가입에 실패했습니다. 인터넷 연결을 확인해 주세요.")
            }
        }
        _uiState.value = JoinUiState.Loading
    }

}