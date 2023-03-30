package com.android.mymindnotes.data.datasources

import android.util.Log
import com.android.mymindnotes.data.retrofit.api.user.*
import com.android.mymindnotes.data.retrofit.model.UserInfoLogin
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
                val userIndex =
                    sharedPreferencesDataSource.sharedPreferenceforUser.getInt("userindex", 0)
                val result = getUserInfoApi.getUserInfo(userIndex)
                _userInfo.value = result
                Log.e("UserInfoCheck", "DataSource - UserInfo emit됨 $result")
            } catch (e: Exception) {
                _error.emit(true)
            }
        }
    }

    // 로그인
    suspend fun loginFlow(email: String, password: String): Flow<Map<String, Object>> = flow {
        val user = UserInfoLogin(email, password)
        val result = loginApi.login(user)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
            // 이렇게 설정해 주어야 로그인을 시도할 때마다 반응하면서도, 화면 재진입 시에 자동으로 다이얼로그가 뜨지 않고(true를 replay해주지 않고), 인터넷이 갑자기 됐을 때 로그인 성공이 돼어 다른 화면으로 넘어가도
            // error가 false로 마지막으로 emit되기 때문에 다른 화면에 영향을 미치지 않는다. (flow는 차례대로 하나 하나 다 emit함)
        }


}