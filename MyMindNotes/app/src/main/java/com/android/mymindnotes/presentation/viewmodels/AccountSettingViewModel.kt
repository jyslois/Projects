package com.android.mymindnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.DeleteUserUseCase
import com.android.mymindnotes.domain.usecase.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecase.UseSharedPreferencesUseCase
import com.android.mymindnotes.hilt.module.IoDispatcher
import com.bumptech.glide.Glide.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val useSharedPreferencesUseCase: UseSharedPreferencesUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
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

    // 회원탈퇴 시 autoSave Sharedpreference clear하는 함수 콜
    suspend fun clearAutoSaveSharedPreferences() {
        useSharedPreferencesUseCase.clearAutoSaveSharedPreferences()
    }

    // collect & emit
    init {
        viewModelScope.launch {

            // 회원 정보 collect & emit
            launch {
                getUserInfoUseCase.userInfo.collect {
                    _userInfo.emit(it)
                    Log.e("UserInfoCheck", "viewModel - emit $it")
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