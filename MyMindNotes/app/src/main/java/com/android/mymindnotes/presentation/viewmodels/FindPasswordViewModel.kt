package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.ChangeUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val changeUserInfoUseCase: ChangeUserInfoUseCase
): ViewModel() {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 임시 비밀번호 보내기 (임시 비밀번호로 비밀번호 변경하기)
    // 버튼 클릭 감지
    private val _sendEmailButton = MutableSharedFlow<Boolean>()
    val sendEmailButton = _sendEmailButton.asSharedFlow()

    suspend fun clickSendEmailButton() {
        _sendEmailButton.emit(true)
    }

    // 임시 비밀번호로 비밀번호 변경하기 결과 저장 플로우
    private val _changeToTemporaryPasswordResult = MutableSharedFlow<Map<String, Object>>()
    val changeToTemporaryPasswordResult = _changeToTemporaryPasswordResult.asSharedFlow()

    // (서버) 임시 비밀번호로 비밀번호 변경하기
    suspend fun changeToTemporaryPassword(email: String, randomPassword: String) {
        changeUserInfoUseCase.changeToTemporaryPassword(email, randomPassword).collect {
            _changeToTemporaryPasswordResult.emit(it)
        }
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