package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.DeleteUserUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ClearLoginStatesUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase,
    private val clearTimeSettingsUseCase: ClearTimeSettingsUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val clearLoginStatesUseCase: ClearLoginStatesUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase
    ) : ViewModel() {

    sealed class AccountSettingUiState {
        object Loading: AccountSettingUiState()
        data class Success(val userInfo: Map<String, Object>?, val deleteUserResult: Map<String, Object>?): AccountSettingUiState()
        data class Error(val error: Boolean): AccountSettingUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<AccountSettingUiState>(AccountSettingUiState.Loading)
    val uiState: StateFlow<AccountSettingUiState> = _uiState


    // 로그아웃 상태 변경
    // 로그아웃 시 autoLoginCheck 상태 변경 함수 콜
    suspend fun saveAutoLoginCheck(state: Boolean) {
        saveAutoLoginStateUseCase(state)
    }


    // (서버) 회원 탈퇴를 위한 함수 콜
    suspend fun deleteUser() {
        deleteUserUseCase().collect {
            _uiState.value = AccountSettingUiState.Success(null, it)
        }
    }

    // 회원탈퇴 시 SharedPreference clear하는 함수
    // autoSave SharedPreference clear하는 함수 콜
    suspend fun clearAutoSaveSharedPreferences() {
        clearLoginStatesUseCase()
    }

    // 알람 설정 SharedPreference clear하는 함수 콜
    suspend fun clearAlarmSharedPreferences() {
        clearAlarmSettingsUseCase()
    }

    // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
    suspend fun clearTimeSharedPreferences() {
        clearTimeSettingsUseCase()
    }

    // stop Alarm
    fun stopAlarm() {
        stopAlarmUseCase()
    }

    // collect & emit
    init {
        viewModelScope.launch {

            val userInfoFlow = getUserInfoUseCase().map { AccountSettingUiState.Success(it, null) } // Flow<AccountSettingUiState.Success>
            val userInfoErrorFlow = getUserInfoUseCase.error.map { AccountSettingUiState.Error(it) }
            val deleteUserErrorFlow = deleteUserUseCase.error.map { AccountSettingUiState.Error(it) }

            merge(userInfoFlow, userInfoErrorFlow, deleteUserErrorFlow).collect {
                _uiState.value = it
            }
        }
    }


}