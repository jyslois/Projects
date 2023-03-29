package com.android.mymindnotes.data.datasources

import android.util.Log
import com.android.mymindnotes.data.retrofit.api.user.*
import com.android.mymindnotes.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
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

//    val userInfoFlow: Flow<Map<String, Object>> = flow {
//        val userIndex = sharedPreferencesDataSource.sharedPreferenceforUser.getInt("userindex", 0)
//        val result = getUserInfoApi.getUserInfo(userIndex)
//        emit(result)
//        Log.e("UserInfo", "DataSource - UserInfo emit됨")
//    }.flowOn(ioDispatcher)

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    // 회원 정보 가져오기
    // 받은 회원정보 저장하는 플로우
    private val _userInfo = MutableStateFlow<Map<String, Object>>(emptyMap())
    val userInfo = _userInfo.asStateFlow()

    // (서버) 회원 정보 가져오기
    suspend fun getUserInfo() {
        withContext(ioDispatcher) {
            try {
                val userIndex = sharedPreferencesDataSource.sharedPreferenceforUser.getInt("userindex", 0)
                val result = getUserInfoApi.getUserInfo(userIndex)
                _userInfo.value = result
                Log.e("UserInfoCheck", "DataSource - UserInfo emit됨 $result")
            } catch (e: Exception) {
                _error.emit(true)
            }
        }
    }


}