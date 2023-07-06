package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: MemberRemoteRepository,
    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val saveUserIndexUseCase: SaveUserIndexUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
) {

//    suspend fun login(email: String, password: String): Flow<Map<String, Object>> {
//        return repository.login(email, password)
//    }

    suspend operator fun invoke(email: String, password: String, isAutoLoginChecked: Boolean, isAutoSaveChecked: Boolean): Flow<LoginResult> {
        return repository.login(email, password).map { response ->
            when (response["code"].toString().toDouble()) {
                5001.0, 5003.0, 5005.0 -> LoginResult.Error(response["msg"] as String)
                5000.0 -> {
                    // 로그인 성공
                    if (isAutoLoginChecked) {
                        // MainActivity에서의 자동 로그인을 위한 상태 저장
                        saveAutoLoginStateUseCase(true)
                    }

                    if (isAutoSaveChecked) {
                        saveAutoSaveStateUseCase(true)
                        saveIdAndPasswordUseCase(email, password)
                    } else {
                        saveAutoSaveStateUseCase(false)
                        saveIdAndPasswordUseCase(null, null)
                    }

                    // 회원 번호 저장
                    saveUserIndexUseCase(response["user_index"].toString().toDouble().toInt())
                    LoginResult.Success
                }
                else -> LoginResult.Error("로그인 중 오류 발생")
            }
        }.catch {
            emit(LoginResult.Error("로그인에 실패했습니다. 인터넷 연결을 확인해 주세요."))
        }
    }

    sealed class LoginResult {
        data class Error(val message: String): LoginResult()
        object Success: LoginResult()
    }

}