package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordMindChoiceViewModel @Inject constructor (
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    sealed class RecordMindChoiceUiState {
        data class Success(val userInfoResult: Map<String, Object>?): RecordMindChoiceUiState()
        data class Error(val error: Boolean): RecordMindChoiceUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<RecordMindChoiceUiState>()
    val uiState: SharedFlow<RecordMindChoiceUiState> = _uiState


    init {
        viewModelScope.launch {

            val getUserInfoFlow = getUserInfoUseCase().map { RecordMindChoiceUiState.Success(it) }
            val getUserInfoErrorFlow = getUserInfoUseCase.error.map { RecordMindChoiceUiState.Error(it) }

            merge(getUserInfoFlow, getUserInfoErrorFlow).collect {
                _uiState.emit(it)
            }

        }
    }

}