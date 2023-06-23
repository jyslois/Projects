package com.android.mymindnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeNickNameUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val changeNickNameUseCase: ChangeNickNameUseCase,
    private val checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase

): ViewModel() {

    sealed class ChangeNickNameUiState {
        data class Success(val nickNameDuplicateCheckResult: Map<String, Object>?, val nickNameChangeResult: Map<String, Object>?): ChangeNickNameUiState()
        data class Error(val error: Boolean): ChangeNickNameUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<ChangeNickNameUiState>()
    val uiState: SharedFlow<ChangeNickNameUiState> = _uiState


    // (서버) 닉네임 중복 체크
    suspend fun checkNickName(nickName: String) {
        checkNickNameDuplicateUseCase(nickName).collect {
            _uiState.emit(ChangeNickNameUiState.Success(nickNameDuplicateCheckResult = it, null))
        }
    }

    // (서버) 닉네임 변경
    suspend fun changeNickName(nickName: String) {
        changeNickNameUseCase(nickName).collect {
            _uiState.emit(ChangeNickNameUiState.Success(null, it))
        }
    }

    // collect & emit
    init {
        viewModelScope.launch {

            val duplicateCheckErrorFlow = checkNickNameDuplicateUseCase.error.map { ChangeNickNameUiState.Error(it) }
            val changeNickNameErrorFlow = changeNickNameUseCase.error.map { ChangeNickNameUiState.Error(it) }

            merge(duplicateCheckErrorFlow, changeNickNameErrorFlow).collect {
                _uiState.emit(it)
            }

        }
    }

}