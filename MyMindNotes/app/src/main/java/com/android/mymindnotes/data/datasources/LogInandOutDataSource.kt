package com.android.mymindnotes.data.datasources

import com.android.mymindnotes.data.retrofit.LoginApi
import javax.inject.Inject

class LogInandOutDataSource @Inject constructor(
    val loginApi: LoginApi
)