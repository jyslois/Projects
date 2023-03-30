package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.ChangeUserInfoUseCase
import com.android.mymindnotes.domain.usecase.DuplicateCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val duplicateCheckUseCase: DuplicateCheckUseCase,
    private val changeUserInfoUseCase: ChangeUserInfoUseCase
): ViewModel() {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 닉네임 중복
    // 닉네임 중복 확인 버튼 클릭 감지
    private val _checkNickNameButton = MutableSharedFlow<Boolean>()
    val checkNickNameButton = _checkNickNameButton.asSharedFlow()

    suspend fun clickCheckNickNameButton() {
        _checkNickNameButton.emit(true)
    }

    // 닉네임 중복 체크 결과 저장 플로우
    private val _nickNameCheckResult = MutableSharedFlow<Map<String, Object>>()
    val nickNameCheckResult = _nickNameCheckResult.asSharedFlow()

    // (서버) 닉네임 중복 체크
    suspend fun checkNickName(nickName: String) {
        duplicateCheckUseCase.checkNickName(nickName).collect {
            _nickNameCheckResult.emit(it)
        }
    }

    // textChange
    // textChange 감지를 위한 SharedFlow
    private val _nickNameInputTextChange = MutableSharedFlow<Boolean>()
    val nickNameInputTextChange = _nickNameInputTextChange.asSharedFlow()

    // textChange 감지 함수
    suspend fun nickNameInputTextChange() {
        _nickNameInputTextChange.emit(true)
    }

    // 닉네임 변경
    // 닉네임 변경 버튼 클릭
    private val _changeNickNameButton = MutableSharedFlow<Boolean>()
    val changeNickNameButton = _changeNickNameButton.asSharedFlow()

    suspend fun clickChangeNickNameButton() {
        _changeNickNameButton.emit(true)
    }

    // 닉네임 변경 결과 저장 플로우
    private val _nickNameChangeResult = MutableSharedFlow<Map<String, Object>>()
    val nickNameChangeResult = _nickNameChangeResult

    // (서버) 닉네임 변경
    suspend fun changeNickName(nickName: String) {
        changeUserInfoUseCase.changeNickName(nickName).collect {
            _nickNameChangeResult.emit(it)
        }
    }

    // collect & emit
    init {
        viewModelScope.launch {
            // duplicateCheck 에러 값 구독
            launch {
                duplicateCheckUseCase.error.collect {
                    _error.emit(it)
                }
            }

            // 닉네임 변경 에러 값 구독
            launch {
                changeUserInfoUseCase.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

}