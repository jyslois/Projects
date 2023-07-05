package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeToTemporaryPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val changeToTemporaryPasswordUseCase: ChangeToTemporaryPasswordUseCase
) : ViewModel() {


    sealed class FindPasswordUiState {
        object Loading : FindPasswordUiState()
        data class Success(val successMessage: String?) : FindPasswordUiState()
        data class Error(val error: String?) : FindPasswordUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<FindPasswordUiState>(FindPasswordUiState.Loading)
    val uiState: StateFlow<FindPasswordUiState> = _uiState


    // (서버) 임시 비밀번호로 비밀번호 변경하기
    suspend fun sendEmailButtonClicked(email: String, randomPassword: String) {

        changeToTemporaryPasswordUseCase(email, randomPassword).collect { result ->
            when (result) {
                is ChangeToTemporaryPasswordUseCase.ChangeToTemporaryPasswordResult.Success -> _uiState.value =
                    FindPasswordUiState.Success(result.message)

                is ChangeToTemporaryPasswordUseCase.ChangeToTemporaryPasswordResult.Error -> _uiState.value =
                    FindPasswordUiState.Error(result.message)
            }
            _uiState.value = FindPasswordUiState.Loading
        }
    }

}