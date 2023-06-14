package com.android.mymindnotes.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.AlarmUseCase
import com.android.mymindnotes.domain.usecases.DeleteUserUseCase
import com.android.mymindnotes.domain.usecases.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.UseMemberSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val useSharedPreferencesUseCase: UseMemberSharedPreferencesUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val alarmUseCase: AlarmUseCase
) : ViewModel() {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 클릭 이벤트
    // 클릭 이벤트 감지 플로우
    private val _changePasswordButton = MutableSharedFlow<Boolean>()
    val changePasswordButton = _changePasswordButton.asSharedFlow()

    private val _changeNicknameButton = MutableSharedFlow<Boolean>()
    val changeNicknameButton = _changeNicknameButton.asSharedFlow()

    private val _logoutButton = MutableSharedFlow<Boolean>()
    val logoutButton = _logoutButton.asSharedFlow()

    private val _withdrawalButton = MutableSharedFlow<Boolean>()
    val withdrawalButton = _withdrawalButton.asSharedFlow()

    // 클릭 이벤트 함수 콜
    suspend fun clickChangePasswordButton() {
        _changePasswordButton.emit(true)
    }

    suspend fun clickChangeNicknameButton() {
        _changeNicknameButton.emit(true)
    }

    suspend fun clickLogoutButton() {
        _logoutButton.emit(true)
    }

    suspend fun clickWithdrawalButton() {
        _withdrawalButton.emit(true)
    }

    // 로그아웃 상태 변경
    // 로그아웃 시 autoLoginCheck 상태 변경 함수 콜
    suspend fun saveAutoLoginCheck(state: Boolean) {
        useSharedPreferencesUseCase.saveAutoLoginCheck(state)
    }

    // 회원정보
    // 회원 정보 플로우
    private val _userInfo = MutableSharedFlow<Map<String, Object>>()
    val userInfo = _userInfo.asSharedFlow()

    // 회원탈퇴
    // 회원 탈퇴 결과 플로우
    private val _deleteUserResult = MutableSharedFlow<Map<String, Object>>()
    val deleteUserResult = _deleteUserResult.asSharedFlow()

    // (서버) 회원 탈퇴를 위한 함수 콜
    suspend fun deleteUser() {
        deleteUserUseCase.deleteUser().collect {
            _deleteUserResult.emit(it)
        }
    }

    // 회원탈퇴 시 SharedPreference clear하는 함수
    // autoSave SharedPreference clear하는 함수 콜
    suspend fun clearAutoSaveSharedPreferences() {
        useSharedPreferencesUseCase.clearAutoSaveSharedPreferences()
    }

    // 알람 설정 SharedPreference clear하는 함수 콜
    suspend fun clearAlarmSharedPreferences() {
        useSharedPreferencesUseCase.clearAlarmSharedPreferences()
    }

    // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
    suspend fun clearTimeSharedPreferences() {
        useSharedPreferencesUseCase.clearTimeSharedPreferences()
    }

    // stop Alarm
    fun stopAlarm(context: Context) {
        alarmUseCase.stopAlarm()
    }

    // collect & emit
    init {
        viewModelScope.launch {

            // 회원 정보 collect & emit
            launch {
                getUserInfoUseCase.getUserInfo().collect {
                    _userInfo.emit(it)
                }
            }

            launch {
                // 회원정보 error collect & emit
                getUserInfoUseCase.error.collect {
                    _error.emit(it)
                }
            }

            // 회원 탈퇴 결과 collect & emit
            launch {
                // 회원탈퇴 error collect & emit
                deleteUserUseCase.error.collect {
                    _error.emit(it)
                }
            }

        }
    }


}