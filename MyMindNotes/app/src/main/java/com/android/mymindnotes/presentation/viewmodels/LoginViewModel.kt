package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.LogInUseCase
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
    private val shared_useCase: UseSharedPreferencesUseCase,
    private val login_useCase: LogInUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    // autoSaveCheck 값을 저장하는 SharedFlow
    private val _autoSaveCheck = MutableSharedFlow<Boolean>(replay = 1)
    val autoSaveCheck get() = _autoSaveCheck.asSharedFlow()

    // autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>(replay = 1)
    val autoLoginCheck get() = _autoLoginCheck.asSharedFlow()

    // Id 값을 저장하는 SharedFlow
    private val _id = MutableSharedFlow<String?>(replay = 1)
    val id = _id.asSharedFlow()

    // Password 값을 저장하는 SharedFlow
    private val _password = MutableSharedFlow<String?>(replay = 1)
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

    // 로그인 결과 값을 저장하는 SharedFlow
    private val _logInResult = MutableSharedFlow<Map<String, Object>>()
    val logInResult = _logInResult.asSharedFlow()

    // 로그인 메서드
    suspend fun login(email: String, password: String) {
        login_useCase.login(email, password)
    }

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

    // get methods - sharedPreferneces

//    suspend fun getIdAndPassword() {
//        shared_useCase.getIdAndPassword()
//    }

    // save methods - SharedPreferneces
    suspend fun saveAutoLoginCheck(state: Boolean) {
        shared_useCase.saveAutoLoginCheck(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        shared_useCase.saveAutoSaveCheck(state)
    }

    suspend fun saveIdAndPassword(id: String?, password: String?) {
        shared_useCase.saveIdAndPassword(id, password)
    }

    suspend fun saveUserIndex(index: Int) {
        shared_useCase.saveUserIndex(index)
    }

    // ViewModel instance가 만들어질 때, autoSaveCheck/autoLoginCheck/id/password 값 불러오기
    init {
        viewModelScope.launch(ioDispatcher) {
            launch {
                // useCase의 함수 리턴 값을 구독해서 viewModel의 SharedFlow에 방출하기.
                shared_useCase.getAutoSave().collect {
                    _autoSaveCheck.emit(it)
                }
            }

            launch {
                shared_useCase.getAutoLogin().collect {
                    _autoLoginCheck.emit(it)
                }
            }


//            launch {
//                shared_useCase.id.collect {
//                    _id.emit(it)
//                }
//            }
//
//            launch {
//                shared_useCase.password.collect {
//                    _password.emit(it)
//                }
//            }

            launch {
                shared_useCase.getId().collect {
                    _id.emit(it)
                }
            }

            launch {
                shared_useCase.getPassword().collect {
                    _password.emit(it)
                }
            }

            launch {
                login_useCase.logInResult.collect {
                    _logInResult.emit(it)
                }
            }

        }
    }
}