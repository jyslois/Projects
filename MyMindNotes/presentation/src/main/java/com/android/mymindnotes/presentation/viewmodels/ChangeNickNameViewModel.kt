package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeNickNameUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val changeNickNameUseCase: ChangeNickNameUseCase,
    private val checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase

) : ViewModel() {

    sealed class ChangeNickNameUiState {
        object Loading : ChangeNickNameUiState()
        data class NickNameDuplicateChecked(val msg: String?) : ChangeNickNameUiState()
        data class NickNameChanged(val msg: String?) : ChangeNickNameUiState()
        data class Error(val error: String) : ChangeNickNameUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<ChangeNickNameUiState>(ChangeNickNameUiState.Loading)
    val uiState: StateFlow<ChangeNickNameUiState> = _uiState.asStateFlow()

    // 테스트용
    var nickNameDuplicateCheckedStateTriggered = false
    var nickNameChangedStateTriggered = false
    var errorStateTriggered = false

    // (서버) 닉네임 중복 체크
    suspend fun checkNickNameButtonClicked(nickName: String) {

        checkNickNameDuplicateUseCase(nickName).collect {
            when {
                it.isSuccess -> {
                    _uiState.value = ChangeNickNameUiState.NickNameDuplicateChecked(it.getOrNull())

                    // 테스트용
                    if (_uiState.value == ChangeNickNameUiState.NickNameDuplicateChecked(it.getOrNull())) {
                            nickNameDuplicateCheckedStateTriggered = true
                    }
                }

                it.isFailure -> {
                    _uiState.value = ChangeNickNameUiState.Error(it.exceptionOrNull()?.message ?: "닉네임 중복 체크 실패.")

                    // 테스트용
                    if (_uiState.value == ChangeNickNameUiState.Error(it.exceptionOrNull()?.message ?: "닉네임 중복 체크 실패.")) {
                        errorStateTriggered = true
                    }
                }
            }
        }
        _uiState.value = ChangeNickNameUiState.Loading
    }

    // (서버) 닉네임 변경
    suspend fun changeNickNameButtonClicked(nickName: String) {

        changeNickNameUseCase(nickName).collect {
            when {
                it.isSuccess -> {
                    _uiState.value = ChangeNickNameUiState.NickNameChanged(it.getOrNull())

                    // 테스트용
                    if (_uiState.value == ChangeNickNameUiState.NickNameChanged(it.getOrNull())) {
                        nickNameChangedStateTriggered = true
                    }

                }

                it.isFailure -> {
                    _uiState.value = ChangeNickNameUiState.Error(it.exceptionOrNull()?.message ?: "닉네임 변경 실패.")

                    // 테스트용
                    if (_uiState.value == ChangeNickNameUiState.Error(it.exceptionOrNull()?.message ?: "닉네임 변경 실패.")) {
                        errorStateTriggered = true
                    }
                }
            }
        }
        _uiState.value = ChangeNickNameUiState.Loading
    }


}