package com.android.mymindnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeNickNameUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val changeNickNameUseCase: ChangeNickNameUseCase,
    private val checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase

): ViewModel() {

    sealed class ChangeNickNameUiState {
        object Loading : ChangeNickNameUiState()

        object NickNameDuplicateChecked: ChangeNickNameUiState()
        object NickNameChanged: ChangeNickNameUiState()
        data class Error(val error: String): ChangeNickNameUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<ChangeNickNameUiState>(ChangeNickNameUiState.Loading)
    val uiState: StateFlow<ChangeNickNameUiState> = _uiState


    // (서버) 닉네임 중복 체크
    suspend fun checkNickName(nickName: String) {
        try {
            checkNickNameDuplicateUseCase(nickName).collect {
                if (it["code"].toString().toDouble() == 1003.0) {
                    _uiState.value = ChangeNickNameUiState.Error(it["msg"] as String)
                } else if (it["code"].toString().toDouble() == 1002.0) {
                    _uiState.value = ChangeNickNameUiState.NickNameDuplicateChecked
                }
            }
        } catch(e: Exception) {
            _uiState.value = ChangeNickNameUiState.Error("닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요.")

        }
        _uiState.value = ChangeNickNameUiState.Loading
    }

    // (서버) 닉네임 변경
    suspend fun changeNickName(nickName: String) {
        try {
            changeNickNameUseCase(nickName).collect {
                if (it["code"].toString().toDouble() == 3001.0) {
                    _uiState.value = ChangeNickNameUiState.Error(it["msg"] as String)
                } else if (it["code"].toString().toDouble() == 3000.0) {
                    _uiState.value = ChangeNickNameUiState.NickNameChanged
                }
            }
        } catch(e: Exception) {
            _uiState.value = ChangeNickNameUiState.Error("닉네임 변경 실패. 인터넷 연결을 확인해 주세요.")
        }
        _uiState.value = ChangeNickNameUiState.Loading
    }


}