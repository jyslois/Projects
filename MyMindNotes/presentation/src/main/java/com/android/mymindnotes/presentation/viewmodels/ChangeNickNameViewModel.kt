package com.android.mymindnotes.presentation.viewmodels

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

) : ViewModel() {

    sealed class ChangeNickNameUiState {
        object Loading : ChangeNickNameUiState()
        object NickNameDuplicateChecked : ChangeNickNameUiState()
        object NickNameChanged : ChangeNickNameUiState()
        data class Error(val error: String) : ChangeNickNameUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<ChangeNickNameUiState>(ChangeNickNameUiState.Loading)
    val uiState: StateFlow<ChangeNickNameUiState> = _uiState


    // (서버) 닉네임 중복 체크
    suspend fun checkNickNameButtonClicked(nickName: String) {

        checkNickNameDuplicateUseCase(nickName).collect { result ->
            when (result) {
                is CheckNickNameDuplicateUseCase.NickNameCheckResult.NotDuplicate ->
                    _uiState.value = ChangeNickNameUiState.NickNameDuplicateChecked

                is CheckNickNameDuplicateUseCase.NickNameCheckResult.Error ->
                    _uiState.value = ChangeNickNameUiState.Error(result.message)
            }
        }
        _uiState.value = ChangeNickNameUiState.Loading

    }

    // (서버) 닉네임 변경
    suspend fun changeNickNameButtonClicked(nickName: String) {

        changeNickNameUseCase(nickName).collect { result ->
            when (result) {
                is ChangeNickNameUseCase.NickNameChangeResult.NickNameChanged ->
                    _uiState.value = ChangeNickNameUiState.NickNameChanged

                is ChangeNickNameUseCase.NickNameChangeResult.Error ->
                    _uiState.value = ChangeNickNameUiState.Error(result.message)
            }
        }
        _uiState.value = ChangeNickNameUiState.Loading
    }


}