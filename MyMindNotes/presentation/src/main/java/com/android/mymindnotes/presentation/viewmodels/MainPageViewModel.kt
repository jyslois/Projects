package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getFirstTimeStateUseCase: GetFirstTimeStateUseCase,
    private val saveFirstTimeStateUseCase: SaveFirstTimeStateUseCase
) : ViewModel() {

    sealed class MainPageUiState {
        object Loading : MainPageUiState()
        object FirstTime : MainPageUiState()
        data class Success(val nickName: String) : MainPageUiState()
        data class Error(val error: String) : MainPageUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<MainPageUiState>(MainPageUiState.Loading)
    val uiState: StateFlow<MainPageUiState> = _uiState

    suspend fun getNickNameFromUserInfo() {

        getUserInfoUseCase().collect {
            when {
                it.isSuccess -> _uiState.value = MainPageUiState.Success(it.getOrThrow().nickname)

                it.isFailure -> {
                    _uiState.value = MainPageUiState.Error(it.exceptionOrNull()?.message ?: "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요.")
                    _uiState.value = MainPageUiState.Loading
                }
            }
        }

    }

    init {

        viewModelScope.launch {

            if (getFirstTimeStateUseCase().first()) {
                _uiState.value = MainPageUiState.FirstTime
                saveFirstTimeStateUseCase(false)
            }

            getNickNameFromUserInfo()

        }
    }

}
