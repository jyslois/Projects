package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.ChangeUserInfoUseCase
import com.android.mymindnotes.domain.usecases.UseMemberSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changeUserInfoUseCase: ChangeUserInfoUseCase,
    private val useSharedPreferencesUseCase: UseMemberSharedPreferencesUseCase
): ViewModel() {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 비밀번호 변경
    // 비밀번호 변경 버튼 클릭
    private val _changePasswordButton = MutableSharedFlow<Boolean>()
    val changePasswordButton = _changePasswordButton.asSharedFlow()

    suspend fun clickChangePasswordButton() {
        _changePasswordButton.emit(true)
    }

    // 비밀번호 변경 결과 저장 플로우
    private val _changePasswordResult = MutableSharedFlow<Map<String, Object>>()
    val changePasswordResult = _changePasswordResult.asSharedFlow()

    // (서버) 비밀번호 변경
    suspend fun changePassword(password: String, originalPassword: String) {
        changeUserInfoUseCase.changePassword(password, originalPassword).collect {
            _changePasswordResult.emit(it)
        }
    }

    // 비밀번호 재저장
    suspend fun savePassword(password: String?) {
        useSharedPreferencesUseCase.savePassword(password)
    }

    init {
        viewModelScope.launch {
            // 비밀번호 변경 에러 값 구독
            launch {
                changeUserInfoUseCase.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

}