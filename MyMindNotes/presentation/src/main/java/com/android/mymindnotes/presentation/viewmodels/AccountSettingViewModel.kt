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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    // ui상태
    private val _uiState = MutableStateFlow<AccountSettingUiState>(AccountSettingUiState.Loading)
    val uiState: StateFlow<AccountSettingUiState> = _uiState

    // 애러 상태
    private val _errorState = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState

    // 로그아웃 상태 변경
    // 로그아웃 시 autoLoginCheck 상태 변경 함수 콜
    suspend fun saveAutoLoginCheck(state: Boolean) {
        saveAutoLoginStateUseCase(state)
    }


    // (서버) 회원 탈퇴를 위한 함수 콜
    suspend fun deleteUser() {
        deleteUserUseCase().collect {
            _uiState.emit(AccountSettingUiState.Success(null, it))
            _errorState.emit(false) // API 호출 성공 시 에러 상태 초기화
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

            // 회원 정보 collect & emit
            launch {
                getUserInfoUseCase().collect {
                    _uiState.emit(AccountSettingUiState.Success(it, null))
                    _errorState.emit(false) // API 호출 성공 시 에러 상태 초기화
                }
            }

            launch {
                // 회원정보 error collect & emit
                getUserInfoUseCase.error.collect {
                    _errorState.emit(it)
//                    _uiState.emit(AccountSettingUiState.Error(it))
                }
            }

            launch {
                // 회원탈퇴 error collect & emit
                deleteUserUseCase.error.collect {
                    _errorState.emit(it)
//                    _uiState.emit(AccountSettingUiState.Error(it))
                }
            }

        }
    }


    sealed class AccountSettingUiState {
        object Loading: AccountSettingUiState()
        data class Success(val userInfo: Map<String, Object>?, val deleteUserResult: Map<String, Object>?): AccountSettingUiState()
//        data class Error(val error: Boolean): AccountSettingUiState()
    }


}