package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordMindChoiceViewModel @Inject constructor (
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    sealed class RecordMindChoiceUiState {
        object Loading: RecordMindChoiceUiState()
        data class Success(val nickName: String): RecordMindChoiceUiState()
        data class Error(val error: String): RecordMindChoiceUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<RecordMindChoiceUiState>(RecordMindChoiceUiState.Loading)
    val uiState: StateFlow<RecordMindChoiceUiState> = _uiState

    fun getNickNameFromUserInfo() {
        viewModelScope.launch {
            try {
                getUserInfoUseCase().collect {
                    val nickName = it["nickname"] as String
                    _uiState.value = RecordMindChoiceUiState.Success(nickName)
                }
            } catch(e: Exception) {
                _uiState.value = RecordMindChoiceUiState.Error("서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요.")
                _uiState.value = RecordMindChoiceUiState.Loading
            }
        }
    }

    init {
        viewModelScope.launch {
            getNickNameFromUserInfo()
        }
    }

}