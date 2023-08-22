package com.android.mymindnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAutoLoginStateUseCase: GetAutoLoginStateUseCase
) : ViewModel() {

    sealed class MainUiState {
        object Loading : MainUiState()
        object AutoLogin : MainUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun checkAndUpdateAutoLoginState() {
        Log.e("순서", "(뷰모델) 뷰모델 생성 및 함수 호출")
        viewModelScope.launch {
            // useCase의 getAutoLogin() 함수에 return된 Flow의 Boolean 값을 관찰해서 viewModel의 uiState에 방출하기.
            getAutoLoginStateUseCase().collect {
                Log.e("순서", "(뷰모델) 데이터 가져오기")
                if (it) {
                    _uiState.emit(MainUiState.AutoLogin)
                }
            }
        }

    }

}