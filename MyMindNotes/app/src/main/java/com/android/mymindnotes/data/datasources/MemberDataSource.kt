package com.android.mymindnotes.data.datasources

import com.android.mymindnotes.data.retrofit.CheckEmailApi
import com.android.mymindnotes.data.retrofit.CheckNickNameApi
import com.android.mymindnotes.data.retrofit.JoinApi
import com.android.mymindnotes.data.retrofit.LoginApi
import javax.inject.Inject

class MemberDataSource @Inject constructor(
    private val _loginApi: LoginApi,
    private val _checkEmailApi: CheckEmailApi,
    private val _checkNickNameApi: CheckNickNameApi,
    private val _joinApi: JoinApi
) {
    // Repository에서 접근 가능하도록 public 변수에 SharedPreference 객체 할당
    val loginApi = _loginApi
    val checkEmailApi = _checkEmailApi
    val checkNickNameApi = _checkNickNameApi
    val joinApi = _joinApi
}