package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getFirstTimeStateUseCase: GetFirstTimeStateUseCase,
    private val saveFirstTimeStateUseCase: SaveFirstTimeStateUseCase
) : ViewModel() {

    sealed class MainPageUiState {
        data class State(val firstTime: Boolean) : MainPageUiState()
        data class Success(val userInfoResult: Map<String, Object>?): MainPageUiState()
        data class Error(val error: Boolean): MainPageUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<MainPageUiState>()
    val uiState: SharedFlow<MainPageUiState> = _uiState


    init {

        viewModelScope.launch {

            val getFirstTimeStateFlow = getFirstTimeStateUseCase().map { MainPageUiState.State(it) }
            val getUserInfoFlow = getUserInfoUseCase().map { MainPageUiState.Success(it) }
            val getUserInfoErrorFlow = getUserInfoUseCase.error.map { MainPageUiState.Error(it) }

            merge(getFirstTimeStateFlow, getUserInfoFlow, getUserInfoErrorFlow).collect {
                _uiState.emit(it)
            }

        }
    }


    // 최초 접속 값을 바꾸기 위한 함수 호출
    suspend fun saveFirstTime(boolean: Boolean) {
        saveFirstTimeStateUseCase(boolean)
    }



}
