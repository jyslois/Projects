package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.JoinUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckEmailDuplicateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val uiState: StateFlow<JoinUiState> = _uiState


    // 이메일 중복 체크 함수 호출
    suspend fun checkEmailButtonClicked(emailInput: String) {

        checkEmailDuplicateUseCase(emailInput).collect { result ->
            when (result) {
                is CheckEmailDuplicateUseCase.CheckEmailDuplicateResult.Success ->
                    _uiState.value = JoinUiState.EmailDuplicateCheckSucceed
                is CheckEmailDuplicateUseCase.CheckEmailDuplicateResult.Error ->
                    _uiState.value = JoinUiState.Error(result.message)
            }
            _uiState.value = JoinUiState.Loading
        }
    }


    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickNameButtonClicked(nickNameInput: String) {
        // (서버) 닉네임 중복 체크
        checkNickNameDuplicateUseCase(nickNameInput).collect { result ->
            when (result) {
                is CheckNickNameDuplicateUseCase.NickNameCheckResult.NotDuplicate ->
                    _uiState.value = JoinUiState.NicknameDuplicateCheckSucceed
                is CheckNickNameDuplicateUseCase.NickNameCheckResult.Error ->
                    _uiState.value = JoinUiState.Error(result.message)
            }
            _uiState.value = JoinUiState.Loading
        }
    }


    // (서버) 회원가입 함수 호출
    suspend fun joinButtonClicked(
        email: String,
        nickname: String,
        password: String,
        birthyear: Int
    ) {

        joinUseCase(email, nickname, password, birthyear).collect { result ->
            when (result) {
                is JoinUseCase.JoinResult.Success ->  _uiState.value = JoinUiState.JoinSucceed
                is JoinUseCase.JoinResult.Error -> _uiState.value = JoinUiState.Error(result.message)
            }
            _uiState.value = JoinUiState.Loading
        }
    }

}