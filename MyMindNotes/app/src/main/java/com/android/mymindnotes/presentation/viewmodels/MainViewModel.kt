package com.android.mymindnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.UseSharedPreferencesUseCase
import com.android.mymindnotes.hilt.module.IoDispatcher
import com.android.mymindnotes.hilt.module.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: UseSharedPreferencesUseCase
): ViewModel() {

//     autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
    val autoLoginCheck get() = _autoLoginCheck.asSharedFlow()

    // 버튼 클릭 감지를 위한 SharedFlow
    private val _login = MutableSharedFlow<Boolean>()
    val login get() = _login.asSharedFlow()
    private val _join = MutableSharedFlow<Boolean>()
    val join get() = _join.asSharedFlow()

    // ViewModel instance가 만들어질 때,
    init {
        viewModelScope.launch {
            launch {
                // useCase의 getAutoLogin() 함수에 return된 Flow의 Boolean 값을 관찰해서 viewModel의 SharedFlow에 방출하기.
                useCase.getAutoLogin().collect {
                    _autoLoginCheck.emit(it)
                }
            }
        }
    }

    suspend fun clickLoginButton() {
        _login.emit(true)
    }

    suspend fun clickJoinButton() {
        _join.emit(true)
    }


}