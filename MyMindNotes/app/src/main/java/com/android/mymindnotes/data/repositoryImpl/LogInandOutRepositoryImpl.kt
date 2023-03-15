package com.android.mymindnotes.data.repositoryImpl

import android.util.Log
import com.android.mymindnotes.data.datasources.LogInandOutDataSource
import com.android.mymindnotes.data.retrofit.UserInfoLogin
import com.android.mymindnotes.domain.repositoryinterfaces.LogInandOutRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class LogInandOutRepositoryImpl @Inject constructor(
   private val logInandOutDataSource: LogInandOutDataSource
) : LogInandOutRepository {

    private val _logInResult = MutableSharedFlow<Map<String, Object>>()
    override val logInResult = _logInResult.asSharedFlow()

    override suspend fun login(email: String, password: String) {
        val user = UserInfoLogin(email, password)
        val result = logInandOutDataSource.loginApi.login(user)
        _logInResult.emit(result)

    }

}