package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: MemberRemoteRepository,
    private val savePasswordUseCase: SavePasswordUseCase
) {

//    // 비밀번호 바꾸기
//    suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
//        return repository.changePassword(password, originalPassword)
//    }

    suspend operator fun invoke(password: String, originalPassword: String): Flow<PasswordChangeResult> {
        return repository.changePassword(password, originalPassword).map { response ->
            when (response["code"].toString().toDouble()) {
                3005.0, 3003.0 -> PasswordChangeResult.Error(response["msg"] as String)
                3002.0 -> {
                    savePasswordUseCase(password)
                    PasswordChangeResult.Success
                }
                else -> PasswordChangeResult.Error("비밀번호 변경 중 오류 발생")
            }
        }.catch {
            emit(PasswordChangeResult.Error("비밀번호 변경 실패. 인터넷 연결을 확인해 주세요."))
        }
    }

    sealed class PasswordChangeResult {
        data class Error(val message: String): PasswordChangeResult()
        object Success: PasswordChangeResult()
    }

}

