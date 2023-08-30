package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.DeleteUserUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
) : ViewModel() {

    var errorStateTriggered = false // 테스트용

    sealed class AccountSettingUiState {
        object Loading : AccountSettingUiState()
        data class Success(val nickName: String, val email: String, val birthyear: Int) : AccountSettingUiState()
        object Logout : AccountSettingUiState()
        data class Withdraw(val msg: String?) : AccountSettingUiState()
        data class Error(val errorMessage: String) : AccountSettingUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<AccountSettingUiState>(AccountSettingUiState.Loading)
    val uiState: StateFlow<AccountSettingUiState> = _uiState.asStateFlow()


    // 로그아웃 버튼 클릭
    suspend fun logoutButtonClicked() {
        // autoLoginCheck 상태 변경
        saveAutoLoginStateUseCase(false)
        // ui 상태 변경
        _uiState.value = AccountSettingUiState.Logout
    }


    // (서버) 회원 탈퇴를 위한 함수 콜
    suspend fun deleteUserButtonClicked() {

        deleteUserUseCase().collect {
            when {
                it.isSuccess -> _uiState.value = AccountSettingUiState.Withdraw(it.getOrNull())

                it.isFailure -> {
                    _uiState.value = AccountSettingUiState.Error(
                        "회원 탈퇴 실패. 인터넷 연결을 확인해 주세요."
                    )

                    // 테스트용
                    if (_uiState.value == AccountSettingUiState.Error("회원 탈퇴 실패. 인터넷 연결을 확인해 주세요.")) {
                        errorStateTriggered = true
                    }

                    _uiState.value = AccountSettingUiState.Loading
                }

            }
        }

    }

    // collect & emit
    init {
        viewModelScope.launch {

            getUserInfoUseCase().collect {
                when {
                    it.isSuccess -> {
                        val userInfo = it.getOrThrow()
                        _uiState.value = AccountSettingUiState.Success(userInfo.nickname, userInfo.email, userInfo.birthyear)
                    }

                    it.isFailure -> {
                        _uiState.value = AccountSettingUiState.Error(
                            "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요."
                        )

                        if (_uiState.value == AccountSettingUiState.Error("서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요.")) {
                            errorStateTriggered = true
                        }

                        _uiState.value = AccountSettingUiState.Loading
                    }
                }
            }
        }
    }


}