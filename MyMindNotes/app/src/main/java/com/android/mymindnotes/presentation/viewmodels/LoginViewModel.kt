package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.UseSharedPreferencesUseCase
import com.android.mymindnotes.hilt.module.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: UseSharedPreferencesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): ViewModel() {
    // autoSaveCheck 값을 저장하는 SharedFlow
    private val _autoSaveCheck = MutableSharedFlow<Boolean>()
    val autoSaveCheck get() = _autoSaveCheck.asSharedFlow()
    // Id 값을 저장하는 SharedFlow
    private val _id = MutableSharedFlow<String>()
    val id = _id.asSharedFlow()
    // Password 값을 저장하는 SharedFlow
    private val _password = MutableSharedFlow<String>()
    val password = _password.asSharedFlow()

    suspend fun getIdAndPassword() {
        useCase.getIdAndPassword()
    }

    // ViewModel instance가 만들어질 때,
    init {
        viewModelScope.launch(ioDispatcher) {
            launch {
                // useCase의 메서드 자동 호출. - SharedPreference에 저장된 autoSaveCheck값을 가져오기 위한.
                useCase.getAutoSave()
            }

            launch {
                // useCase의 SharedFlow에 저장된 값 관찰해서 viewModel의 SharedFlow에 방출하기.
                useCase.autoSaveCheck.collect {
                    _autoSaveCheck.emit(it)
                }
            }

            launch {
                useCase.id.collect {
                    _id.emit(it)
                }
            }

            launch {
                useCase.password.collect {
                    _password.emit(it)
                }
            }

        }
    }
}