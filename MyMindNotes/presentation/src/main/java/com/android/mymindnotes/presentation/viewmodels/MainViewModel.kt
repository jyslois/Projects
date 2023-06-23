package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAutoLoginStateUseCase: GetAutoLoginStateUseCase
): ViewModel() {


    sealed class MainUiState {
        data class State(val autoLoginStateResult: Boolean): MainUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState: SharedFlow<MainUiState> = _uiState


    // ViewModel instance가 만들어질 때,
    init {
        viewModelScope.launch {
            launch {
                // useCase의 getAutoLogin() 함수에 return된 Flow의 Boolean 값을 관찰해서 viewModel의 SharedFlow에 방출하기.
                getAutoLoginStateUseCase().collect {
                    _uiState.emit(MainUiState.State(it))
                }
            }
        }
    }



}