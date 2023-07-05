package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.DeleteUserUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
) : ViewModel() {

    sealed class AccountSettingUiState {
        object Loading : AccountSettingUiState()
        data class Success(val userInfo: Map<String, Object>?) : AccountSettingUiState()
        object Logout : AccountSettingUiState()
        object Withdraw : AccountSettingUiState()
        data class Error(val errorMessage: String) : AccountSettingUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<AccountSettingUiState>(AccountSettingUiState.Loading)
    val uiState: StateFlow<AccountSettingUiState> = _uiState


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
                it.isSuccess ->
                    // ui 상태 변경
                    _uiState.value = AccountSettingUiState.Withdraw

                it.isFailure -> {
                    _uiState.value = AccountSettingUiState.Error(it.exceptionOrNull()?.message ?: "회원 탈퇴 실패. 인터넷 연결을 확인해 주세요.")
                    _uiState.value = AccountSettingUiState.Loading
                }

            }
        }

    }

    // collect & emit
    init {
        viewModelScope.launch {
            try {
                val userInfo = getUserInfoUseCase().first()
                _uiState.value = AccountSettingUiState.Success(userInfo)
            } catch (e: Exception) {
                _uiState.value =
                    AccountSettingUiState.Error("회원 정보를 불러오는 데 실패했습니다. 인터넷 연결을 확인해 주세요.")
                _uiState.value = AccountSettingUiState.Loading
            }
        }
    }


}