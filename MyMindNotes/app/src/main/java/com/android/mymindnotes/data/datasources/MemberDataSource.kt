package com.android.mymindnotes.data.datasources

import android.util.Log
import com.android.mymindnotes.data.retrofit.api.user.*
import com.android.mymindnotes.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MemberDataSource @Inject constructor(
     val loginApi: LoginApi,
     val checkEmailApi: CheckEmailApi,
     val checkNickNameApi: CheckNickNameApi,
     val joinApi: JoinApi,
    private val getUserInfoApi: GetUserInfoApi,
     val deleteUserApi: DeleteUserApi,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    val userInfoFlow: Flow<Map<String, Object>> = flow {
        val userIndex = sharedPreferencesDataSource.sharedPreferenceforUser.getInt("userindex", 0)
        val result = getUserInfoApi.getUserInfo(userIndex)
        emit(result)
        Log.e("UserInfo", "DataSource - UserInfo emit됨")
    }.flowOn(ioDispatcher)


}