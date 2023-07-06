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

        changeToTemporaryPasswordUseCase(email, randomPassword).collect {
            when {
                it.isSuccess -> _uiState.value = FindPasswordUiState.Success(it.getOrNull())

                it.isFailure -> _uiState.value = FindPasswordUiState.Error(it.exceptionOrNull()?.message ?: "임시 비밀번호 발송에 실패했습니다. 인터넷 연결을 확인해 주세요.")
            }
            _uiState.value = FindPasswordUiState.Loading
        }
    }

}