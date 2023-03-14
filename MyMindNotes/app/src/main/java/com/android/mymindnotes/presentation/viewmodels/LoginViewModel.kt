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
) : ViewModel() {
    // autoSaveCheck 값을 저장하는 SharedFlow
    private val _autoSaveCheck = MutableSharedFlow<Boolean>()
    val autoSaveCheck get() = _autoSaveCheck.asSharedFlow()

    // autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
    val autoLoginCheck get() = _autoLoginCheck.asSharedFlow()

    // Id 값을 저장하는 SharedFlow
    private val _id = MutableSharedFlow<String>()
    val id = _id.asSharedFlow()

    // Password 값을 저장하는 SharedFlow
    private val _password = MutableSharedFlow<String>()
    val password = _password.asSharedFlow()

    // 버튼 상태 저장하는 SharedFlow
    private val _autoSaveButton = MutableSharedFlow<Boolean>()
    val autoSaveButton get() = _autoSaveButton.asSharedFlow()
    private val _autoLoginButton = MutableSharedFlow<Boolean>()
    val autoLoginButton get() = _autoLoginButton.asSharedFlow()

    private val _findPasswordButton = MutableSharedFlow<Boolean>()
    val findPasswordButton get() = _findPasswordButton.asSharedFlow()
    private val _loginButton = MutableSharedFlow<Boolean>()
    val loginButton get() = _loginButton.asSharedFlow()


    // 버튼 클릭 메서드
    suspend fun clickAutoSaveBox() {
        _autoSaveButton.emit(true)
    }

    suspend fun clickAutoLoginBox() {
        _autoLoginButton.emit(true)
    }

    suspend fun clickFindPasswordButton() {
        _findPasswordButton.emit(true)
    }

    suspend fun clickLoginButton() {
        _loginButton.emit(true)
    }

    // get methods
    suspend fun getIdAndPassword() {
        useCase.getIdAndPassword()
    }

    // save methods
    suspend fun saveAutoLoginCheck(state: Boolean) {
        useCase.saveAutoLoginCheck(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        useCase.saveAutoSaveCheck(state)
    }

    suspend fun saveIdAndPassword(id: String?, password: String?) {
        useCase.saveIdAndPassword(id, password)
    }

    // ViewModel instance가 만들어질 때, autoSaveCheck/autoLoginCheck/id/password 값 불러오기
    init {
        viewModelScope.launch(ioDispatcher) {
            launch {
                useCase.getAutoLogin()
                useCase.getAutoSave()
            }

            launch {
                // useCase의 SharedFlow에 저장된 값 관찰해서 viewModel의 SharedFlow에 방출하기.
                useCase.autoSaveCheck.collect {
                    _autoSaveCheck.emit(it)
                }
            }

            launch {
                useCase.autoLoginCheck.collect {
                    _autoLoginCheck.emit(it)
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